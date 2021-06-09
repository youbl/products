using System.Linq;
using RemindClock.Repository.Model;

namespace RemindClock.Repository
{
    /// <summary>
    /// 版本仓储层
    /// </summary>
    public class VersionRepository : BaseRepository<Version>
    {
        public Version FindFirst()
        {
            return FindAll().FirstOrDefault();
        }

        /// <summary>
        /// 本地版本号加1
        /// </summary>
        public int PlusVersion()
        {
            var version = FindFirst() ?? new Version();
            version.ClientVersion++;
            Save(version);
            return version.ClientVersion;
        }
    }
}