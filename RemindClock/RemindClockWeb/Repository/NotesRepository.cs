using System.Collections.Generic;
using Beinet.Repository.Repositories;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Repository
{
    interface NotesRepository : JpaRepository<Notes, int>
    {
        [Query("select * from #{#entityName} where userId=?1 order by id", NativeQuery = true)]
        List<Notes> GetByUser(int userId);
    }
}