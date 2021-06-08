using System.Collections.Generic;
using Beinet.Core.Cron;
using RemindClock.Repository.Model;
using RemindClock.Services.NoteOperation;

namespace RemindClock.Services
{
    class SchedueService
    {
        /// <summary>
        /// 所有通知器，比如钉钉通知、短信通知、窗体通知等
        /// </summary>
        List<INoteAlert> AllAlerts = new List<INoteAlert>();

        private NotesService notesService = new NotesService();

        //private bool isRunning = false;


        public SchedueService()
        {
            AllAlerts.Add(new NoteAlertByForm());
            AllAlerts.Add(new NoteAlertByDingDing());
        }

        /// <summary>
        /// cron格式参考： https://github.com/youbl/DemoCode
        /// 表达式由6个或7个由空格分隔的字符串组成，第7个年可选
        /// </summary>
        [Scheduled(cron: "* * * * * *")]
        public void Begin()
        {
//            if (isRunning)
//                return;
//            isRunning = true;

            var allNotes = notesService.FindAll();
            foreach (var note in allNotes)
            {
                var detailId = 0;
                foreach (var noteDetail in note.Details)
                {
                    if (notesService.IsNoteTime(note.Id, detailId, noteDetail))
                    {
                        StartNote(note);
                    }

                    detailId++;
                }
            }

//            isRunning = false;
        }

        private void StartNote(Notes note)
        {
            foreach (var noteAlert in AllAlerts)
            {
                noteAlert.Alert(note);
            }
        }
    }
}