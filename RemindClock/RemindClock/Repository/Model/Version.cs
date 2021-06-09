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
    }
}