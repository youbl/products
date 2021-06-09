using System;
using System.Linq;
using Version = RemindClock.Repository.Model.Version;

namespace RemindClock.Repository
{
    /// <summary>
    /// 版本仓储层
    /// </summary>
    public class VersionRepository : BaseRepository<Version>
    {
        /// <summary>
        /// 返回第一个版本号数据
        /// </summary>
        /// <returns></returns>
        public Version FindFirst()
        {
            var arr = FindAll();
            if (arr.Count > 1)
            {
                throw new Exception("不能有2个以上的版本信息");
            }

            var ret = arr.FirstOrDefault();
            if (ret != null)
            {
                return ret;
            }

            ret = new Version();
            return Save(ret);
        }

        /// <summary>
        /// 本地版本号加1
        /// </summary>
        public int PlusVersion()
        {
            var version = FindFirst();
            version.ClientVersion++;
            Save(version);
            return version.ClientVersion;
        }
    }
}