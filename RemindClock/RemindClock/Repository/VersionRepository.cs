using System.Linq;
using RemindClock.Repository.Model;

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
            return FindAll().FirstOrDefault() ?? new Version();
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