package com.chaoip.ipproxy;

import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.util.AliPayHelper;
import com.chaoip.ipproxy.util.WechatPay;
import com.chaoip.ipproxy.util.config.WechatConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * SmsHelperTest
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/29 21:37
 */
@SpringBootTest
@ActiveProfiles("unittest")
public class WechatPayTest {
    @Autowired
    private WechatPay wechatPay;

    @Test
    public void test_verify() throws Exception {
        WechatConfig config = new WechatConfig();
        config.setPayurl("https://api.mch.weixin.qq.com/v3/pay/transactions/native");
        config.setQueryurl("https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}?mchid={mchid} ");
        config.setAppId("wx开头的一串");
        config.setPrivateKey("特别长的一串，导出证书里apiclient_key.pem的内容");
        config.setMchId("商户id，一串数字");
        config.setMchSerialNo("证书号");
        config.setApiV3Key("API V3版本的密钥");
        config.setCallback("http://localhost:8801/user/payback");

        PayOrder url = wechatPay.getPayUrl("", 1, "我在单元测试 支付一下", config);
        System.out.println(url);
    }

    @Test
    public void test_query() throws Exception {
        // 支付成功的订单
        // {"alipay_trade_query_response":{"code":"10000","msg":"Success","buyer_logon_id":"lmj***@sandbox.com","buyer_pay_amount":"0.00","buyer_user_id":"2088102181222795","buyer_user_type":"PRIVATE","invoice_amount":"0.00","out_trade_no":"CHDL202012091033297193249","point_amount":"0.00","receipt_amount":"0.00","send_pay_date":"2020-12-09 10:34:47","total_amount":"1000.00","trade_no":"2020120922001422790501328313","trade_status":"TRADE_SUCCESS"},"sign":"RxfBuPoXiyBEAqEnlSWMLOjcKN5RWaZA6l0psxrktUMm5GOuaCswNAauyNkcbuV5makWxnhid9SWTW+NmWR0K/DIoCGVkRHJyYbCKaHvAJluVS88az6FQNJLY5UTIKv7yjLty+9PFitXDyzqaccvI+9LgSanvkpm5RlOqXVOEdm32mNNaEIaqsbuqOe1zC+4ZZeFVPAbqJ2VwSWv9QQcnPdlw32p9Pr1bxA8/oaklYZlpqa1zgc8Dg9Y6UIwHh8eg1LHCZoUgrCHzSr4kBelBjRb+tlzTCSwWMVt2tEhMA52yJG6eZ7XzvvcZRGt1Y/SfvnFpKaK9TetNPWypTTKwg=="}
        // boolean ret = aliPayHelper.queryPayResult("", "CHDL202012091033297193249");
        // System.out.println(ret);
    }
}
