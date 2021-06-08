using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Beinet.Repository;
using RemindClockWeb.Repository;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Services
{
    public class NotesService
    {
        private readonly UsersRepository usersRepository = ProxyLoader.GetProxy<UsersRepository>();
        private readonly NotesRepository notesRepository = ProxyLoader.GetProxy<NotesRepository>();
        private readonly NotesDetailRepository detailRepository = ProxyLoader.GetProxy<NotesDetailRepository>();

        public List<Notes> GetNotesByUser(int userId)
        {
            var ret = notesRepository.GetByUser(userId);
            foreach (var note in ret)
            {
                note.Details = detailRepository.GetByNoteId(note.Id);
            }

            return ret;
        }
    }
}