namespace RemindClock.Utils.Sms
{
    /// <summary>
    /// 阿里短信配置
    /// </summary>
    public class AliSmsConfig
    {
        /// <summary>
        /// 阿里云短信api接口
        /// </summary>
        public string ApiUrl { get; set; } = "https://dysmsapi.aliyuncs.com/?Action=SendSms";

        public string AK { get; set; }
        public string SK { get; set; }

        /// <summary>
        /// 短信签名，必须在阿里云先审核通过
        /// </summary>
        public string SignName { get; set; }

        /// <summary>
        /// 短信模板，也必须在阿里云先审核通过
        /// </summary>
        public string TemplateCode { get; set; }

        /// <summary>
        /// 短信模板对应的变量json，如 "{\"title\":\"{title}\"}"
        /// </summary>
        public string TemplateParamJson { get; set; }

        /// <summary>
        /// 配置无效还是有效
        /// </summary>
        public bool IsInvalid
        {
            get
            {
                if (string.IsNullOrEmpty(AK)
                    || string.IsNullOrEmpty(SK)
                    || string.IsNullOrEmpty(ApiUrl)
                    || string.IsNullOrEmpty(SignName)
                    || string.IsNullOrEmpty(TemplateCode)
                    || string.IsNullOrEmpty(TemplateParamJson))
                {
                    return true;
                }

                return !ValidParaFormat;
            }
        }

        public bool ValidParaFormat => TemplateParamJson != null && TemplateParamJson.Contains("{title}");
    }
}