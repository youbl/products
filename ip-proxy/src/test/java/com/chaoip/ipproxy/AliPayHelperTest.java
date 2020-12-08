package com.chaoip.ipproxy;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.util.AliPayHelper;
import com.chaoip.ipproxy.util.VerifyHelper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.UnsupportedEncodingException;

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
    public void test_verify() throws AlipayApiException, UnsupportedEncodingException {
        String url = aliPayHelper.getPayUrl("", 1, "我在单元测试", "单元测试支付一下");
        System.out.println(url);
    }
}
