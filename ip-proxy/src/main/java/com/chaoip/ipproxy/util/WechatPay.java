package com.chaoip.ipproxy.util;

import com.chaoip.ipproxy.repository.entity.OrderStatus;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.service.SiteConfigService;
import com.chaoip.ipproxy.util.config.WechatConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;

/**
 * 微信支付类
 * 文档:  https://github.com/wechatpay-apiv3/wechatpay-apache-httpclient
 *
 * @author youbl
 * @version 1.0
 * @date 2021/02/05 22:29
 */
@Component
@Slf4j
public class WechatPay {
    private static ObjectMapper mapper;

    static {
        mapper = Jackson2ObjectMapperBuilder.json()
                .modules(new SimpleModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)  // 禁用写时间戳，Could not read JSON: Cannot construct instance of `java.time.LocalDateTime`
                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES) // 反序列化时，忽略大小写
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)// 忽略未知属性
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToDisable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)   // 忽略未知的类型，比如本项目里不存在的class
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                // .featuresToDisable(MapperFeature.USE_ANNOTATIONS)
                .featuresToEnable()
                .build();
    }

    protected final SiteConfigService configService;

    public WechatPay(SiteConfigService configService) {
        this.configService = configService;
    }

    public WechatConfig getConfig() throws Exception {
        return configService.getWechatConfig();
    }

    /**
     * 调用支付接口.
     * 注意：金额单位为分
     *
     * @param account   支付人账号
     * @param moneyCent 支付金额，单位分
     * @return 支付宝支付url
     * @throws Exception 异常
     */
    public PayOrder getPayUrl(String account, int moneyCent, String title, String description) throws Exception {
        WechatConfig config = getConfig();
        return getPayUrl(account, moneyCent, title, description, config);
    }

    /**
     * 调用支付接口.
     * 注意：金额单位为分
     *
     * @param account   支付人账号
     * @param moneyCent 支付金额，单位分
     * @param config    支付配置
     * @return 支付宝支付url
     * @throws Exception 异常
     */
    public PayOrder getPayUrl(String account, int moneyCent, String title, String description, WechatConfig config) throws Exception {
        if (StringUtils.isEmpty(config.getPayurl()) ||
                StringUtils.isEmpty(config.getAppId()) ||
                StringUtils.isEmpty(config.getPrivateKey())) {
            throw new RuntimeException("请先进行微信支付相关配置");
        }
        String orderNo = AliBase.getTransId();
        String payUrl = createPayUrl(moneyCent, title, orderNo, config);
        log.info("getPayUrl账号:{} 参数:{} 成功结果:{}", account, moneyCent, payUrl);
        return PayOrder.builder()
                .orderNo(orderNo)
                .name(account)
                .money(moneyCent)
                .payType(2)
                .payUrl(payUrl)
                .title(title)
                .description(description)
                .status(OrderStatus.NO_PAY)
                .build();
    }

    private String createPayUrl(int moneyCent, String description, String orderNo, WechatConfig config) throws Exception {
        HttpPost request = new HttpPost(config.getPayurl());
        request.setHeader("Content-Type", "application/json;charset=UTF-8");
        request.setHeader("Accept", "application/json");

        // 生成的json，不允许有null值，否则会报错
        String jsonPara = getJson(config, moneyCent, description, orderNo);
        StringEntity se = new StringEntity(jsonPara);
        se.setContentType("text/json");
        request.setEntity(se);

        // 加载商户私钥（privateKey：私钥字符串） pem文件，或p12文件里解析出来的
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(config.getPrivateKey().getBytes("utf-8")));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3秘钥）
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(config.getMchId(), new PrivateKeySigner(config.getMchSerialNo(), merchantPrivateKey)), config.getApiV3Key().getBytes("utf-8"));

        // 初始化httpClient
        try (CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(config.getMchId(), config.getMchSerialNo(), merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build()) {
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("微信支付接口报错:" + EntityUtils.toString(response.getEntity()));
            }

//            StringBuilder sb = new StringBuilder("响应Headers:\n");
//            for (Header header : response.getAllHeaders()) {
//                sb.append("  ")
//                        .append(header.getName())
//                        .append(": ")
//                        .append(header.getValue())
//                        .append("\n");
//            }
            HttpEntity entity = response.getEntity();
            String url = EntityUtils.toString(entity);
//            sb.append("响应内容:\n").append(url);
//
//            System.out.println(sb);
            return url;
        }
            /*
成功响应的Headers:
  Server: nginx
  Date: Mon, 15 Mar 2021 15:17:48 GMT
  Content-Type: application/json; charset=utf-8
  Content-Length: 52
  Connection: keep-alive
  Keep-Alive: timeout=8
  Cache-Control: no-cache, must-revalidate
  X-Content-Type-Options: nosniff
  Request-ID: 089CF5BD820610F80118B3C78C58209D0628D3D303-0
  Content-Language: zh-CN
  Wechatpay-Nonce: 83708e51607856da275b819f827e51e4
  Wechatpay-Signature: iMJGu7MA8pu1gD8tKNY9hZZRUIVt4g6jtfQi6jfUP3dernN/+l8Syv3WQpfEXMYtRS6XacR49P1TLvVf6my4VBgFdZ2rRrHEiMU9lwqj5jWTjXjplPDbMuFuCuyHMklnbwG5BSmXcE7XFhiBUEGv40zBjxXLGZinyAQPnr+PP/6v4b3vAy/unfXS0fxgRDCwHwT45drpIoSD9J3JsWYjWR/LjTQBP8D8q0ay7o6CAx29IlYwMcvTUPnkIKBTCyEYNjiLt4NeV+++6P0HnxQ4zX/6WmgcEPaylzclGIhZn1+TVw4VexPPysUfPMzcjeO1fpNpByqPmZpE/jKVWp+J8g==
  Wechatpay-Timestamp: 1615821468
  Wechatpay-Serial: 312114998B8088EF41EF1C5753EABAF3A4F02310
响应内容:
{"code_url":"weixin://wxpay/bizpayurl?pr=NLDxVDczz"}
            * */
    }

    /**
     * 查询支付状态
     *
     * @param order
     * @return
     */
    public boolean queryPayResult(PayOrder order) throws Exception {
        // https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_2.shtml
        // https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{out_trade_no}?mchid={mchid}
        WechatConfig config = getConfig();
        String queryUrl = config.getQueryurl()
                .replace("{out_trade_no}", order.getOrderNo())
                .replace("{mchid}", config.getMchId());

        HttpGet request = new HttpGet(queryUrl);
        request.setHeader("Accept", "application/json");

        // 加载商户私钥（privateKey：私钥字符串） pem文件，或p12文件里解析出来的
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(config.getPrivateKey().getBytes("utf-8")));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3秘钥）
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(config.getMchId(), new PrivateKeySigner(config.getMchSerialNo(), merchantPrivateKey)), config.getApiV3Key().getBytes("utf-8"));

        // 初始化httpClient
        try (CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(config.getMchId(), config.getMchSerialNo(), merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build()) {
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("微信支付接口报错:" + EntityUtils.toString(response.getEntity()));
            }

            StringBuilder sb = new StringBuilder("响应Headers:\n");
            for (Header header : response.getAllHeaders()) {
                sb.append("  ")
                        .append(header.getName())
                        .append(": ")
                        .append(header.getValue())
                        .append("\n");
            }
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            sb.append("响应内容:\n").append(content);

            System.out.println(sb);
        }

        return false;
    }

    private String getJson(WechatConfig config, int moneyCent, String description, String orderNo) throws Exception {
        WebchatPayDto dto = new WebchatPayDto();
        //dto.setTimeExpire("2021-06-08T10:34:56+08:00");
        dto.setAppid(config.getAppId());
        dto.setMchid(config.getMchId());

        dto.setDescription(description);
        dto.setOutTradeNo(orderNo);
        dto.setNotifyUrl(config.getCallback());

        WebchatPayDto.AmountDTO amountDTO = new WebchatPayDto.AmountDTO();
        dto.setAmount(amountDTO);
        amountDTO.setTotal(moneyCent);
        amountDTO.setCurrency("CNY");

        //dto.setGoodsTag("WXG");
        //dto.setAttach("p");

        return mapper.writeValueAsString(dto);
        /*
{
	"time_expire": "2018-06-08T10:34:56+08:00",
	"amount": {
		"total": 100,
		"currency": "CNY"
	},
	"mchid": "1230000109",
	"description": "Image形象店-深圳腾大-QQ公仔",
	"notify_url": "https://www.weixin.qq.com/wxpay/pay.php",
	"out_trade_no": "1217752501201407033233368018",
	"goods_tag": "WXG",
	"appid": "wxd678efh567hg6787",
	"attach": "自定义数据说明",
	"detail": {
		"invoice_id": "wx123",
		"goods_detail": [{
			"goods_name": "iPhoneX 256G",
			"wechatpay_goods_id": "1001",
			"quantity": 1,
			"merchant_goods_id": "商品编码",
			"unit_price": 828800
		}, {
			"goods_name": "iPhoneX 256G",
			"wechatpay_goods_id": "1001",
			"quantity": 1,
			"merchant_goods_id": "商品编码",
			"unit_price": 828800
		}],
		"cost_price": 608800
	},
	"scene_info": {
		"store_info": {
			"address": "广东省深圳市南山区科技中一道10000号",
			"area_code": "440305",
			"name": "腾讯大厦分店",
			"id": "0001"
		},
		"device_id": "013467007045764",
		"payer_client_ip": "14.23.150.211"
	}
}
        * */
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
