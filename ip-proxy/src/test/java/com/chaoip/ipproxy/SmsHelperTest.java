package com.chaoip.ipproxy;

import com.chaoip.ipproxy.util.SmsHelper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * SmsHelperTest
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/29 21:37
 */
//@SpringBootTest
//@ActiveProfiles("unittest")
public class SmsHelperTest {
    @Test
    public void test_send() {
        String phone = "15980726586";
        String code = "axya-test";
        String ret = SmsHelper.send(phone, code);
        // {"RequestId":"8D3B28FA-7B15-4607-8ED5-100D3B37ABF3","Message":"OK","BizId":"837306406658750141^0","Code":"OK"}
        System.out.println(ret);
        Assert.assertTrue(ret.contains("\"Code\":\"OK\""));

        phone = "1598072658";
        ret = SmsHelper.send(phone, code);
        // {"RequestId":"5E98D8F4-7C6F-4657-860A-C2502643CA26","Message":"1598072658invalid mobile number","Code":"isv.MOBILE_NUMBER_ILLEGAL"}
        System.out.println(ret);
        Assert.assertTrue(ret.contains("MOBILE_NUMBER_ILLEGAL"));
    }
}
