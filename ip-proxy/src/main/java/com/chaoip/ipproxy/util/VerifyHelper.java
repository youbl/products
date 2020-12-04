package com.chaoip.ipproxy.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayUserCertifyOpenInitializeRequest;
import com.alipay.api.request.ZhimaCustomerCertificationCertifyRequest;
import com.alipay.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.alipay.api.response.AlipayUserCertifyOpenInitializeResponse;
import com.alipay.api.response.ZhimaCustomerCertificationCertifyResponse;
import com.alipay.api.response.ZhimaCustomerCertificationInitializeResponse;
import com.chaoip.ipproxy.util.config.VerifyConfig;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * VerifyHelper
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/1 21:38
 */
@Component
public class VerifyHelper {
    private final VerifyConfig config;

    public VerifyHelper(VerifyConfig verifyConfig) {
        this.config = verifyConfig;
    }

    // https://opendocs.alipay.com/apis/api_2/alipay.user.certify.open.initialize
    // 原始文档是有问题的，参考： https://opensupport.alipay.com/support/helpcenter/172/201602489641
    public void getBizcode2(String name, String identity) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(config.getUrl(), config.getAppId(), config.getPrivateKey(),
                config.getFormat(), config.getCharset(), config.getPublicKey(), config.getSignType());
        AlipayUserCertifyOpenInitializeRequest request = new AlipayUserCertifyOpenInitializeRequest();
//        request.setBizContent("{" +
//                "\"outer_order_no\":\"ZGYD201809132323000001234\"," +
//                "\"biz_code\":\"FACE\"," +
//                "\"identity_param\":\"{\"identity_type\":\"CERT_INFO\",\"cert_type\":\"IDENTITY_CARD\",\"cert_name\":\"收委\",\"cert_no\":\"260104197909275964\"}\"," +
//                "\"merchant_config\":\"{\"return_url\":\"https://cc.chidaoni.com/test/ip.aspx\"}\"," +
//                "\"face_contrast_picture\":\"xydasf==\"" +
//                "  }");
        request.setBizContent("{" +
                "\"outer_order_no\":\"Z3230034023001234\"," +
                "\"biz_code\":\"FACE\"," +
                "\"identity_param\":{\"identity_type\":\"CERT_INFO\",\"cert_type\":\"IDENTITY_CARD\",\"cert_name\":\"真实姓名\",\"cert_no\":\"身份证号\"}," +
                "\"merchant_config\":{\"return_url\":\"https://cc.chidaoni.com/test/ip.aspx\"}" +
                "}");

        AlipayUserCertifyOpenInitializeResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            // {"alipay_user_certify_open_initialize_response":{"code":"10000","msg":"Success","certify_id":"e63e21d979ef24934d5aa384847e7560"},"sign":"Y2uvAsHhoSz116l8KO/QiL05qy/lgJyVzlqO6tmn9fQxPpKCtIKYwILLjgYd6h6rp9TargX5JEf9BJqSObnDOz7CDWpTI57Pwf8trRjsI/I9i2rLC6dTMEhTe4Oe1Z2DNtz8+Bie+ahHbharBBPpxAiGPaT5LdpFfE/7zKWeIATZ7jgl8F0jcSgwvonbVXq9orZvL1pgbb4oQS3hioulqG2nHOH1q+xTRYfLc/mHJ0PE4pATw7U+FeAHvqQnlkYH7keYXdK73JFVhP4Enjv6sBzJuLiv4Dp4l8w1wjBTFh2DM+ZjvCrboED+vVF4hlV9QA+9cld5c2lPVJ/TKDo/Bg=="}
            System.out.println("调用成功");
        } else {
            // {"alipay_user_certify_open_initialize_response":{"code":"40004","msg":"Business Failed","sub_code":"unknown-sub-code","sub_msg":"未知的错误码UNKNOWN_ERROR"},"sign":"YzgKLj1Idl1w5K+fgopB7fT4o7NiEzLsXqHZR9C75ATl9vJBTz0B/eSyTmdokY0SZwtGtj31tadaR1DMh2Q4Z0zePuKwrGU8HOb0QDy4P2wfZfd92wmfhN5r7kCVrgIePwufmxsNxQUV3rYPxBJpEcuQa8MxT8yRnYPQKEchqoUzXAQhAja2r4hPEOZf1zdmjjj3JmEHN274xNSxIZ3ZtPghMexpuMvHe+reP/6oCS7kgk0EOD1U80xIoYz5M61lasaZq9Pd8D1yYlfGiWVY+lHGoKmdsE7+BvvnC3YHi+bL0G3uAw7y23RSloy86gbsogvvV+/bQVdSh7SzZoPWmQ=="}
            System.out.println("调用失败");
        }
    }

    public void getBizcode(String name, String identity) throws AlipayApiException {
        String transId = getTransId();
        String param = "{\"identity_type\":\"CERT_INFO\",\"cert_type\":\"IDENTITY_CARD\",\"cert_name\":\"" + name + "\",\"cert_no\":\"" + identity + "\"}";

        AlipayClient alipayClient = new DefaultAlipayClient(config.getUrl(), config.getAppId(), config.getPrivateKey(),
                config.getFormat(), config.getCharset(), config.getPublicKey(), config.getSignType());
        ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
        request.setBizContent("{" +
                "\"transaction_id\":\"" + transId + "\"," +
                "\"product_code\":\"w1010100000000002978\"," +
                "\"biz_code\":\"FACE\"," +
                "\"identity_param\":\"" + param + "\"," +
                "\"merchant_config\":\"{}\"," +
                "\"ext_biz_param\":\"{}\"," +
                "\"linked_merchant_id\":\"\"," +
                "\"face_contrast_picture\":\"\"" +
                "  }");
        ZhimaCustomerCertificationInitializeResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    public void verify(String bizNo) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(config.getUrl(), config.getAppId(), config.getPrivateKey(),
                config.getFormat(), config.getCharset(), config.getPublicKey(), config.getSignType());
        ZhimaCustomerCertificationCertifyRequest request = new ZhimaCustomerCertificationCertifyRequest();
        request.setBizContent("{\"biz_no\":\"" + bizNo + "\"}");// ZM201612013000000393900404029253
        ZhimaCustomerCertificationCertifyResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    private static String getTransId() {
        String ret = "CHDL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        ret += StrHelper.getRndStr(4, 1);
        return ret;
    }
}
