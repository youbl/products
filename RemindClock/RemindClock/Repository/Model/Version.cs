using System;
using RemindClock.Utils.Sms;

namespace RemindClock.Repository.Model
{
    /// <summary>
    /// 版本类，用于同步策略判断
    /// </summary>
    public class Version : BaseModel
    {
        /// <summary>
        /// 客户端版本号
        /// </summary>
        public int ClientVersion { get; set; }

        /// <summary>
        /// 最近一次同步的服务端版本号
        /// </summary>
        public int ServerVersion { get; set; }

        /// <summary>
        /// 最近同步时间
        /// </summary>
        public DateTime LastSyncTime { get; set; }

        /// <summary>
        /// 是否启用同步
        /// </summary>
        public bool SyncEnable { get; set; }

        /// <summary>
        /// 同步服务器地址
        /// </summary>
        public string SyncUrl { get; set; }

        /// <summary>
        /// 同步账号
        /// </summary>
        public string SyncUser { get; set; }

        /// <summary>
        /// 同步密钥
        /// </summary>
        public string SyncToken { get; set; }

        /// <summary>
        /// 阿里云的短信配置
        /// </summary>
        public AliSmsConfig SmsConfig { get; set; }
    }
}