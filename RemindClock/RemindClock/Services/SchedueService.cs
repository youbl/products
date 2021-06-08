using System;
using System.Collections.Generic;
using System.Threading;
using Beinet.Core.Cron;
using NLog;
using RemindClock.Repository.Model;
using RemindClock.Services.NoteOperation;

namespace RemindClock.Services
{
    class SchedueService
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

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
        /// 主调方法，每秒轮询所有任务
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
                // 改用线程进行通知避免阻塞和异常
                ThreadPool.UnsafeQueueUserWorkItem(state =>
                {
                    var tmpNote = (Notes) note;
                    try
                    {
                        noteAlert.Alert(tmpNote);
                    }
                    catch (Exception exp)
                    {
                        logger.Error(exp, note.Title + ":" + noteAlert.GetType().Name);
                    }
                }, note);
            }
        }
    }
}