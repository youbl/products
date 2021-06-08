using Beinet.Repository.Repositories;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Repository
{
    interface NotesDetailRepository : JpaRepository<NoteDetail, int>
    {
    }
}