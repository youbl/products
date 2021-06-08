using Beinet.Repository.Repositories;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Repository
{
    interface UsersRepository : JpaRepository<Users, int>
    {
    }
}