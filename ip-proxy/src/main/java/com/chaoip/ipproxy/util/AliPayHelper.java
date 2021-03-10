package com.chaoip.ipproxy.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.chaoip.ipproxy.repository.entity.OrderStatus;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.service.SiteConfigService;
import com.chaoip.ipproxy.util.config.AliConfigBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 支付宝支付类
 * 文档: https://opendocs.alipay.com/open/01didh
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/7 22:29
 */
@Component
@Slf4j
public class AliPayHelper extends AliBase {

    public AliPayHelper(SiteConfigService configService) {
        super(configService);
    }

    @Override
    public AliConfigBase getConfig() throws Exception {
        return configService.getAliPayConfig();
    }

    /**
     * 调用支付宝支付接口.
     * 注意：金额单位为分
     *
     * @param account   支付人账号
     * @param moneyCent 支付金额，单位分
     * @return 支付宝支付url
     * @throws JsonProcessingException 序列化异常
     * @throws AlipayApiException      阿里异常
     */
    public PayOrder getPayUrl(String account, int moneyCent, String title, String description) throws Exception {
        this.config = getConfig();

        String orderNo = getTransId();
        String callbackUrl = getCallback(orderNo); // 回转地址
        if (StringUtils.isEmpty(title)) {
            title = "支付";
        } else {
            // title = URLEncoder.encode(title, "UTF-8");
        }
        if (StringUtils.isEmpty(description)) {
            description = "支付金额(分): " + moneyCent;
        } else {
            // description = URLEncoder.encode(description, "UTF-8");
        }
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest(); //创建API对应的request
        alipayRequest.setReturnUrl(callbackUrl);
        alipayRequest.setNotifyUrl(""); // 异步回调的通知地址
        alipayRequest.setBizContent("{" +
                "\"out_trade_no\":\"" + orderNo + "\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "\"total_amount\":" + transMoneyToYuan(moneyCent) + "," +
                "\"subject\":\"" + title + "\"," +
                "\"body\":\"" + description + "\"" +
                //"    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                //"    \"extend_params\":{\"sys_service_provider_id\":\"2088511833207846\"}" +
                "}"); //填充业务参数

        AlipayTradePagePayResponse response = getClient().pageExecute(alipayRequest, "GET");
        if (response.isSuccess()) {
            String payUrl = response.getBody();
            log.info("getPayUrl账号:{} 参数:{} 成功结果:{}", account, moneyCent, payUrl);
            return PayOrder.builder()
                    .orderNo(orderNo)
                    .name(account)
                    .money(moneyCent)
                    .payType(1)
                    .payUrl(payUrl)
                    .title(title)
                    .description(description)
                    .status(OrderStatus.NO_PAY)
                    .build();
        }
        log.error("getPayUrl账号:{} 参数 {} 失败结果: {}", account, moneyCent, response.getBody());
        throw new RuntimeException("请求支付宝支付失败");
    }

    /**
     * 查询支付宝支付订单状态
     *
     * @param order 订单
     * @throws JsonProcessingException 序列化异常
     * @throws AlipayApiException      阿里异常
     */
    public boolean queryPayResult(PayOrder order) throws Exception {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{\"out_trade_no\":\"" + order.getOrderNo() + "\"}");// +
        // "\"trade_no\":\"\"," +                   // 支付宝交易号
        // "\"org_pid\":\"2088101117952222\"," +    // 银行间联用
        // "      \"query_options\":[" +
        // "        \"trade_settle_info\"" +
        // "      ]" +
        // "  }");
        AlipayTradeQueryResponse response = getClient().execute(request);
        if (response.isSuccess()) {
            log.info("queryPayResult账号:{} 参数:{} 调用成功:{}", order.getName(), order.getOrderNo(), response.getBody());
            // WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
            if (response.getTradeStatus().equals("TRADE_SUCCESS")) {
                int realPayMoney = transMoneyToCent(response.getTotalAmount()); // 实际支付金额
                if (realPayMoney != order.getMoney()) {
                    throw new RuntimeException("订单金额:" + order.getMoney() + " 与支付宝不匹配:" + realPayMoney);
                }
                return true;
            }
            return false;
        }

        log.error("queryPayResult账号:{} 参数 {} 调用失败: {}", order.getName(), order.getOrderNo(), response.getBody());
        throw new RuntimeException("支付宝支付失败");
    }

    /**
     * 把分的单位转换为元，并保留2位小数
     *
     * @param moneyCent 金额，单位分
     * @return 单位:元的字符串
     */
    private static String transMoneyToYuan(int moneyCent) {
        return String.format("%.2f", moneyCent / 100F);
    }

    /**
     * 把元的单位转换为分，并取整
     *
     * @param moneyStr 金额，单位元
     * @return 单位:分的整数金额
     */
    private static int transMoneyToCent(String moneyStr) {
        float money = Float.parseFloat(moneyStr) * 100;
        return (int) money;
    }
}
