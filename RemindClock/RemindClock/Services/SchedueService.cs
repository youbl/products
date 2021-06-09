﻿using Beinet.Core.Cron;

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

        /// <summary>
        /// 与服务端数据同步的方法，每分钟执行1次
        /// </summary>
        [Scheduled(cron: "10 * * * * *")]
        public void SyncNotes()
        {
            //if (isRunning)
            //    return;
            //isRunning = true;
            syncService.BeginSync();
        }
    }
}