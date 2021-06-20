using System;
using System.Collections.Generic;
using System.IO;
using System.Web;
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

        public object Send(string signName, string templateCode, string phone, string code)
        {
            var url = END_POINT;
            var version = "2017-05-25";

            var param = new Dictionary<string, string>();
            param["Action"] = "SendSms";
            param["PhoneNumbers"] = phone;
            param["SignName"] = signName;
            param["TemplateCode"] = templateCode;
            param["TemplateParam"] = "{\"code\":\"" + code + "\"}";
            return AccessAli<DomainTopList>(url, version, param);
        }
    }
}