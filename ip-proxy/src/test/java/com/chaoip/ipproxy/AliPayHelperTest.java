package com.chaoip.ipproxy;

import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.util.AliPayHelper;
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
public class AliPayHelperTest {
    @Autowired
    private AliPayHelper aliPayHelper;

    @Test
    public void test_verify() throws Exception {
        PayOrder url = aliPayHelper.getPayUrl("", 1, "我在单元测试", "单元测试支付一下");
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
