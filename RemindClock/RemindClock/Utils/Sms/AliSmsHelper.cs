using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RemindClock.Utils.Sms
{
    /// <summary>
    /// 阿里短信发送服务类,
    /// api文档： https://help.aliyun.com/document_detail/101414.htm
    /// </summary>
    public class AliSmsHelper
    {
        /// <summary>
        /// 阿里云短信api接口
        /// </summary>
        public string ApiUrl { get; set; } = "https://dysmsapi.aliyuncs.com/?Action=SendSms";

        public string AK { get; set; } = "aaa";
        public string SK { get; set; } = "bbb";

        /// <summary>
        /// 短信签名，必须在阿里云先审核通过
        /// </summary>
        public string SignName { get; set; }

        /// <summary>
        /// 短信模板，也必须在阿里云先审核通过
        /// </summary>
        public string TemplateCode { get; set; }

        public string TemplateParamJson { get; set; } = "{\"code\":\"{code}\"}";
    }
}