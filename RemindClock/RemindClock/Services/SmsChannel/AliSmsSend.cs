using RemindClock.Utils.Sms;

namespace RemindClock.Services.SmsChannel
{
    class AliSmsSend : SmsSend
    {
        private AliSmsConfig config;

        public AliSmsSend(AliSmsConfig config)
        {
            this.config = config;
        }

        public void Send(string phone, string content)
        {
            if (config == null)
                return;

            var helper = new AliSmsHelper(config);
            helper.Send(phone, content);
        }
    }
}