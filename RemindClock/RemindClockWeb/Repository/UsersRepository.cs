using Beinet.Repository.Repositories;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Repository
{
    interface UsersRepository : JpaRepository<Users, int>
    {
        [Query("select * from #{#entityName} where account=?1 limit 1", NativeQuery = true)]
        Users GetByAccount(string account);
    }
}