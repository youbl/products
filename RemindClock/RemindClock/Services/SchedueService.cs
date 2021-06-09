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
        private NotesService notesService = new NotesService();
        private SyncService syncService = new SyncService();

        private bool isRunning = false;

        /// <summary>
        /// 主调方法，每秒轮询所有任务
        /// cron格式参考： https://github.com/youbl/DemoCode
        /// 表达式由6个或7个由空格分隔的字符串组成，第7个年可选
        /// </summary>
        [Scheduled(cron: "* * * * * *")]
        public void ScanAllNotes()
        {
//            if (isRunning)
//                return;
//            isRunning = true;
            notesService.ScanAllNote();
//            isRunning = false;
        }

        [Scheduled(cron: "*/10 * * * * *")]
        public void SyncNotes()
        {
            //if (isRunning)
            //    return;
            //isRunning = true;
            syncService.BeginSync();
        }
    }
}