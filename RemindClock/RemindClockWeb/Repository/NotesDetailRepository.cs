﻿using System.Collections.Generic;
using Beinet.Repository.Repositories;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Repository
{
    public interface NotesDetailRepository : JpaRepository<NoteDetail, int>
    {
        [Query("select * from #{#entityName} where noteId=?1 order by id", NativeQuery = true)]
        List<NoteDetail> GetByNoteId(int noteId);

        [Modifing]
        [Query("delete a from noteDetail a, notes b where a.noteid=b.id and b.userId=?1", NativeQuery = true)]
        int DeleteByUser(int userId);
    }
}