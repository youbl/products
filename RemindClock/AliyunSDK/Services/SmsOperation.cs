using System;
using System.Collections.Generic;
using AliyunSDK.Model;

namespace AliyunSDK.Services
{
    /// <summary>
    /// 阿里短信发送服务类,
    /// api文档： https://help.aliyun.com/document_detail/101414.htm
    /// </summary>
    public class SmsOperation : AliOperation
    {
        private const string END_POINT = "https://dysmsapi.aliyuncs.com/";

        public SmsOperation(string accessKeyId, string accessKeySecret) : base(accessKeyId,
            accessKeySecret)
        {
        }

        public object Send(string phone, string signName, string templateCode, string paramJson)
        {
            var url = END_POINT;
            var version = "2017-05-25";

            var param = new Dictionary<string, string>();
            param["Action"] = "SendSms";
            param["PhoneNumbers"] = phone;
            param["SignName"] = signName;
            param["TemplateCode"] = templateCode;
            param["TemplateParam"] = paramJson; // "{\"title\":\"" + code + "\"}";
            return AccessAli<DomainTopList>(url, version, param);
        }
    }
}