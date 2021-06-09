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
    }
}