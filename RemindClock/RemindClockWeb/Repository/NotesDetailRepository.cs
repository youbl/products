using System.Collections.Generic;
using Beinet.Repository.Repositories;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Repository
{
    interface NotesDetailRepository : JpaRepository<NoteDetail, int>
    {
        [Query("select * from #{#entityName} where noteId=?1 order by id", NativeQuery = true)]
        List<NoteDetail> GetByNoteId(int noteId);
    }
}