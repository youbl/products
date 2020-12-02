package com.chaoip.ipproxy.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.ZhimaCustomerCertificationCertifyRequest;
import com.alipay.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.alipay.api.response.ZhimaCustomerCertificationCertifyResponse;
import com.alipay.api.response.ZhimaCustomerCertificationInitializeResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * VerifyHelper
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/1 21:38
 */
public class VerifyHelper {
    private static String url = "https://openapi.alipaydev.com/gateway.do"; // "https://openapi.alipay.com/gateway.do"
    private static String appId = "123";
    private static String privateKey = "MIIE456";
    private static String publicKey = "MIIBIjANBgkq789"; // ALIPAY_PUBLIC_KEY
    private static String encode = "GBK";
    private static String signType = "RSA2";

    public static void getBizcode() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(url,appId,privateKey,"json",encode,publicKey,signType);
        ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
        request.setBizContent("{" +
                "\"transaction_id\":\"ZGYD201610252323000001234\"," +
                "\"product_code\":\"w1010100000000002978\"," +
                "\"biz_code\":\"FACE\"," +
                "\"identity_param\":\"{\\\"identity_type\\\":\\\"CERT_INFO\\\",\\\"cert_type\\\":\\\"IDENTITY_CARD\\\",\\\"cert_name\\\":\\\"收委\\\",\\\"cert_no\\\":\\\"260104197909275964\\\"}\"," +
                "\"merchant_config\":\"{}\"," +
                "\"ext_biz_param\":\"{}\"," +
                "\"linked_merchant_id\":\"2088721630869411\"," +
                "\"face_contrast_picture\":\"xydasf==\"" +
                "  }");
        ZhimaCustomerCertificationInitializeResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    public static void verify() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(url, appId, privateKey,
                "json", encode, publicKey, signType);
        ZhimaCustomerCertificationCertifyRequest request = new ZhimaCustomerCertificationCertifyRequest();
        request.setBizContent("{\"biz_no\":\"ZM201612013000000393900404029253\"}");
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
