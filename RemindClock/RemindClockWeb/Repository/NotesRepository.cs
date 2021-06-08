using Beinet.Repository.Repositories;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Repository
{
    interface NotesRepository : JpaRepository<Notes, int>
    {
    }
}