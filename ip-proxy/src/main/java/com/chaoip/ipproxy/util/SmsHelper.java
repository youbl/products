package com.chaoip.ipproxy.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * SmsHelper
 * 短信发送辅助类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/29 19:57
 */
public final class SmsHelper {
    private static final String AK = "aa";
    private static final String SK = "bb";
    private static final String SIGN = "超巴云计算"; // 短信里的签名，如 百度网盘
    private static final String TEMPLATE = "SMS_205881852"; // 短信模板

    /**
     * 发送短信验证码，并返回结果
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 结果
     */
    public static String send(String phone, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", AK, SK);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SIGN);
        request.putQueryParameter("TemplateCode", TEMPLATE);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            return response.getData();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
