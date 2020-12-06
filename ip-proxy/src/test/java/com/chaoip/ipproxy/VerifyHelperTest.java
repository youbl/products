package com.chaoip.ipproxy;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.util.VerifyHelper;
import org.junit.Assert;
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
public class VerifyHelperTest {
    @Autowired
    private VerifyHelper verifyHelper;

    @Test
    public void test_verify() throws AlipayApiException {
        // verifyHelper.getBizcode("张三", "100000199901010011");
    }
}
