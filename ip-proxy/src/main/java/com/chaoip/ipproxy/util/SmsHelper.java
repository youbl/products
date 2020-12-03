package com.chaoip.ipproxy.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.chaoip.ipproxy.util.config.SmsConfig;
import org.springframework.stereotype.Component;

/**
 * SmsHelper
 * 短信发送辅助类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/29 19:57
 */
@Component
public final class SmsHelper {
    private final SmsConfig config;

    public SmsHelper(SmsConfig config) {
        this.config = config;
    }


    /**
     * 发送短信验证码，并返回结果
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 结果
     */
    public String send(String phone, String code) {
        DefaultProfile profile = DefaultProfile.getProfile(config.getRegion(), config.getAk(), config.getSk());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(config.getDomain());
        request.setSysVersion(config.getVersion());
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", config.getRegion());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", config.getSign());
        request.putQueryParameter("TemplateCode", config.getTemplate());
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            return response.getData();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
