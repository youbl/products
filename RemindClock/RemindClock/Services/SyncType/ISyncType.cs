using RemindClock.Repository.Model;

namespace RemindClock.Services.SyncType
{
    /// <summary>
    /// 数据同步接口定义
    /// </summary>
    public interface ISyncType
    {
        /// <summary>
        /// 根据本地版本与服务端版本，判断是否用当前类进行同步
        /// </summary>
        /// <param name="version"></param>
        /// <param name="serverVersion"></param>
        /// <returns></returns>
        bool Sync(Version version, int serverVersion);
    }
}