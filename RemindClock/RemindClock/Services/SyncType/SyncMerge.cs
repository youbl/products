using System;
using Version = RemindClock.Repository.Model.Version;

namespace RemindClock.Services.SyncType
{
    /// <summary>
    /// 服务器与客户端进行合并的实现。
    /// 暂时抛异常，后续再考虑
    /// </summary>
    public class SyncMerge : ISyncType
    {
        public bool Match(Version version, int serverVersion)
        {
            // 上次同步版本 < 服务端版本 && 本地版本 > 上次同步版本
            // 表示2边同时变更了，有冲突
            return (version.ServerVersion < serverVersion
                    && version.ClientVersion > version.ServerVersion);
        }

        public void Sync()
        {
            throw new Exception("冲突，请人工合并");
        }
    }
}