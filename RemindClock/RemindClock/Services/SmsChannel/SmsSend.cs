namespace RemindClock.Services.SmsChannel
{
    /// <summary>
    /// 短信发送接口
    /// </summary>
    public interface SmsSend
    {
        /// <summary>
        /// 发短信
        /// </summary>
        /// <param name="phone">手机号</param>
        /// <param name="content">内容</param>
        void Send(string phone, string content);
    }
}