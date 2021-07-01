using AliyunSDK.Services;

namespace RemindClock.Utils.Sms
{
    /// <summary>
    /// 阿里短信发送服务类,
    /// api文档： https://help.aliyun.com/document_detail/101414.htm
    /// </summary>
    public class AliSmsHelper
    {
        private AliSmsConfig config;

        public AliSmsHelper(AliSmsConfig config)
        {
            this.config = config;
        }

        public void Send(string phone, string title)
        {
            if (string.IsNullOrEmpty(title)
                || string.IsNullOrEmpty(config.AK)
                || string.IsNullOrEmpty(config.SK))
            {
                return;
            }

            title = title.Replace('"', '\'');

            var paraJson = config.TemplateParamJson.Replace("{title}", title);
            var operation = new SmsOperation(config.AK, config.SK);
            operation.Send(phone, config.SignName, config.TemplateCode, paraJson);
        }
    }
}