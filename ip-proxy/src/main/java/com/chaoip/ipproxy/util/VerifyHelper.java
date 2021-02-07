package com.chaoip.ipproxy.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayUserCertifyOpenCertifyRequest;
import com.alipay.api.request.AlipayUserCertifyOpenInitializeRequest;
import com.alipay.api.request.AlipayUserCertifyOpenQueryRequest;
import com.alipay.api.response.AlipayUserCertifyOpenCertifyResponse;
import com.alipay.api.response.AlipayUserCertifyOpenInitializeResponse;
import com.alipay.api.response.AlipayUserCertifyOpenQueryResponse;
import com.chaoip.ipproxy.repository.entity.RealOrder;
import com.chaoip.ipproxy.service.SiteConfigService;
import com.chaoip.ipproxy.util.config.AliConfigBase;
import com.chaoip.ipproxy.util.config.VerifyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 支付宝身份认证辅助类。
 * 1、调用 getBizcode 方法获取一个id
 * 2、调用 beginValidate 方法获取要跳转到的支付宝url，生成二维码，用手机支付宝扫码认证
 * 3、调用 queryValidate 方法，验证是否认证成功
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/1 21:38
 */
@Component
@Slf4j
public class VerifyHelper extends AliBase {

    public VerifyHelper(SiteConfigService configService) {
        super(configService);
    }

    @Override
    protected AliConfigBase getConfig() throws Exception {
        return configService.getVerifyConfig();
    }

    /**
     * 获取实名认证短码地址
     *
     * @param orderNo 订单号
     * @return url
     */
    public String getShortUrl(String orderNo) {
        return checkUrl(((VerifyConfig) config).getShortUrl()) + orderNo;
    }

    /**
     * 获取认证跳转地址，并返回认证订单对象
     *
     * @param account  账号
     * @param realName 实名
     * @param identity 身份证
     * @return 对象
     * @throws AlipayApiException 异常
     */
    public RealOrder getVerifyData(String account, String realName, String identity) throws Exception {
        this.config = getConfig();

        String orderNo = getTransId();
        String callbackUrl = getCallback(orderNo);

        String certId = getBizcode(realName, identity, orderNo, callbackUrl);
        String url = beginValidate(certId, account);
        RealOrder ret = RealOrder.builder()
                .orderNo(orderNo)
                .certId(certId)
                .aliUrl(url)
                .name(account)
                .realName(realName)
                .identity(identity)
                .build();
        ret.setOrderNo(orderNo);

        return ret;
    }

    // https://opendocs.alipay.com/apis/api_2/alipay.user.certify.open.initialize
    // 原始文档是有问题的，参考： https://opensupport.alipay.com/support/helpcenter/172/201602489641
    public String getBizcode(String name, String identity, String orderNo, String callbackUrl) throws Exception {

        AlipayUserCertifyOpenInitializeRequest request = new AlipayUserCertifyOpenInitializeRequest();
        request.setBizContent("{" +
                "\"outer_order_no\":\"" + orderNo + "\"," +
                "\"biz_code\":\"FACE\"," +
                "\"identity_param\":{\"identity_type\":\"CERT_INFO\",\"cert_type\":\"IDENTITY_CARD\",\"cert_name\":\"" + name + "\",\"cert_no\":\"" + identity + "\"}," +
                "\"merchant_config\":{\"return_url\":\"" + callbackUrl + "\"}" +
                "}");

        AlipayUserCertifyOpenInitializeResponse response = getClient().execute(request);
        if (response.isSuccess()) {
            log.info("getBizcode参数 {} {} {} 成功结果: {}", orderNo, name, identity, response.getBody());
            return response.getCertifyId();
            // {"alipay_user_certify_open_initialize_response":{"code":"10000","msg":"Success","certify_id":"e63e21d979ef24934d5aa384847e7560"},"sign":"Y2uvAsHhoSz116l8KO/QiL05qy/lgJyVzlqO6tmn9fQxPpKCtIKYwILLjgYd6h6rp9TargX5JEf9BJqSObnDOz7CDWpTI57Pwf8trRjsI/I9i2rLC6dTMEhTe4Oe1Z2DNtz8+Bie+ahHbharBBPpxAiGPaT5LdpFfE/7zKWeIATZ7jgl8F0jcSgwvonbVXq9orZvL1pgbb4oQS3hioulqG2nHOH1q+xTRYfLc/mHJ0PE4pATw7U+FeAHvqQnlkYH7keYXdK73JFVhP4Enjv6sBzJuLiv4Dp4l8w1wjBTFh2DM+ZjvCrboED+vVF4hlV9QA+9cld5c2lPVJ/TKDo/Bg=="}
        }
        log.error("getBizcode参数 {} {} {} 失败结果: {}", orderNo, name, identity, response.getBody());
        throw new RuntimeException("getBizcode 调用失败");
        // {"alipay_user_certify_open_initialize_response":{"code":"40004","msg":"Business Failed","sub_code":"unknown-sub-code","sub_msg":"未知的错误码UNKNOWN_ERROR"},"sign":"YzgKLj1Idl1w5K+fgopB7fT4o7NiEzLsXqHZR9C75ATl9vJBTz0B/eSyTmdokY0SZwtGtj31tadaR1DMh2Q4Z0zePuKwrGU8HOb0QDy4P2wfZfd92wmfhN5r7kCVrgIePwufmxsNxQUV3rYPxBJpEcuQa8MxT8yRnYPQKEchqoUzXAQhAja2r4hPEOZf1zdmjjj3JmEHN274xNSxIZ3ZtPghMexpuMvHe+reP/6oCS7kgk0EOD1U80xIoYz5M61lasaZq9Pd8D1yYlfGiWVY+lHGoKmdsE7+BvvnC3YHi+bL0G3uAw7y23RSloy86gbsogvvV+/bQVdSh7SzZoPWmQ=="}
    }

    // https://opendocs.alipay.com/apis/api_2/alipay.user.certify.open.certify
    // https://opendocs.alipay.com/open/01brh4#H5%20%E9%A1%B5%E9%9D%A2%E6%8E%A5%E5%85%A5 新的文档，按该文档说明，仅支持GET方式，然后要把返回的链接转成二维码

    /**
     * 获取人脸认证地址
     *
     * @param certifyId getBizcode方法返回的支付宝认证唯一id
     * @param account   仅用于日志，无其它作用
     * @return 地址
     * @throws AlipayApiException 异常
     */
    public String beginValidate(String certifyId, String account) throws Exception {
        AlipayUserCertifyOpenCertifyRequest request = new AlipayUserCertifyOpenCertifyRequest();
        request.setBizContent("{\"certify_id\":\"" + certifyId + "\"}");
        // 必须用get，会返回一个url，转成二维码，用支付宝扫码即可
        AlipayUserCertifyOpenCertifyResponse response = getClient().pageExecute(request, "GET");
        if (response.isSuccess()) {
            log.info("beginValidate账号:{} 参数:{} 成功结果:{}", account, certifyId, response.getBody());
            return response.getBody();
        }
        log.error("beginValidate账号:{} 参数 {} 失败结果: {}", account, certifyId, response.getBody());
        throw new RuntimeException("实名认证失败");
    }

    /**
     * https://opendocs.alipay.com/apis/api_2/alipay.user.certify.open.query/
     * <p>
     * 查询人脸认证结果
     *
     * @param certifyId getBizcode方法返回的支付宝认证唯一id
     * @param account   仅用于日志，无其它作用
     * @return 地址
     * @throws AlipayApiException 异常
     */
    public boolean queryValidate(String certifyId, String account) throws Exception {
        AlipayUserCertifyOpenQueryRequest request = new AlipayUserCertifyOpenQueryRequest();
        request.setBizContent("{\"certify_id\":\"" + certifyId + "\"}");
        AlipayUserCertifyOpenQueryResponse response = getClient().execute(request);
        if (response.isSuccess()) {
            // List<String> result = response.getPassed(); // 居然取不到值，要自行判断
            log.info("beginValidate账号:{} 参数:{} 调用成功:{}", account, certifyId, response.getBody());
            return response.getBody().indexOf("\"passed\":\"T\"") > 0;
        }
        log.error("beginValidate账号:{} 参数 {} 失败结果: {}", account, certifyId, response.getBody());
        throw new RuntimeException("实名认证失败");
    }

    /* 停用的接口  https://opendocs.alipay.com/apis/api_8/zhima.customer.certification.certify
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
    */


}
