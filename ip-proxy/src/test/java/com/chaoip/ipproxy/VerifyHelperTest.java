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

        // 验证通过的，返回值：
        // {"alipay_user_certify_open_query_response":{"code":"10000","msg":"Success","material_info":"{}","passed":"T"},"sign":"kJm8uymN1eyOQWG/9EGFo3pUdGuY5rzRnNCI8Qjm9jAo+4EfoO/IRGkqVnsqSBDZbBomWCGW4ClArlcJCiPEgxxGjrXrM4bzsEEQQjre+LOjdAK4JoiV9mM9FlYcAcqzyVfplYpqpkYyoobbRSjQrYaeYKloPKdiTZxNDlC6n5Z2Scg46aYRR+4jtTXZfXezRCpXrtUifaRa6G7noQ+6bsOcmNY10tTDSH3EaCdL9CemwXCUnh+C2mVxwcml5nlTZcGputfkI4Iwc5BULSkXGY7km5SG4dLZAKSim9LGyV0AMqA2ershpRKZw+dQHkUvXPMyCOiryY6kYpDTweuFZw=="}
        String certId = "5d8f42338d5059a93a1001f86a492fa7";
        Assert.assertTrue(verifyHelper.queryValidate(certId, ""));

        // 验证失败的, 下面2个请求的返回值，都是
        // {"alipay_user_certify_open_query_response":{"code":"10000","msg":"Success","material_info":"{}","passed":"F"},"sign":"E0KtrrZlaKwGpcPKbfx0Q3PjC11uIU60wv/C6rVpJDQa8MnIAHvN5BSl16CEc60sx1AgOxtBMm03Vspqa5uYYsJmXeDN6+6u6C2uegSttwCeDS2P9D/os/UXPIMEW6dEq/0oOh2gLI9jy2NoBddSC9EGaLnCUyyUo7Y6eRSQhLDAqKr1KgSD3mKd3ow1ErRkdl4RPjy9ady1J5EOAoPuDvDZPQOaxS5p65BZA1Du0XAp/1bFHnAd5mdolIx+nqXNUSxG4ZaOM11p54I+K7WJJ5g9n0SWXRv+SHYa1tewcRyxvNOvaMokVPl/BL9fXHiCChv3SLFeQGDoQqCNM2nEJQ=="}
        certId = "92a1a3f28029749664c28b6b60310dbc";
        Assert.assertFalse(verifyHelper.queryValidate(certId, ""));
        certId = "a969cc70aa2585e77947970598d1cb12";
        Assert.assertFalse(verifyHelper.queryValidate(certId, ""));

        // 未验证的，返回值，与验证失败的相同
        certId = "7099e1faa82276c73fd9b418db929c90";
        Assert.assertFalse(verifyHelper.queryValidate(certId, ""));
    }
}
