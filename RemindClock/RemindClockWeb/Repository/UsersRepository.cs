using Beinet.Repository.Repositories;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Repository
{
    public interface UsersRepository : JpaRepository<Users, int>
    {
        [Query("select * from #{#entityName} where account=?1 limit 1", NativeQuery = true)]
        Users GetByAccount(string account);

        /// <summary>
        /// 版本号加1
        /// </summary>
        /// <param name="userId">用户id</param>
        /// <param name="oldVersion">更新前的版本号</param>
        /// <returns>更新行数</returns>
        [Query("update #{#entityName} set version=version+1 where id=?1 and version=?2", NativeQuery = true)]
        int IncrVersion(int userId, int oldVersion);
    }
}