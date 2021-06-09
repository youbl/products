using System;
using System.Collections.Generic;
using System.Threading;
using NLog;
using RemindClock.Repository;
using RemindClock.Repository.Model;
using RemindClock.Services.NoteOperation;
using RemindClock.Services.NoteType;

namespace RemindClock.Services
{
    class NotesService
    {
        private static readonly ILogger logger = LogManager.GetCurrentClassLogger();
        private readonly NotesRepository notesRepository = new NotesRepository();
        private readonly VersionRepository versionRepository = new VersionRepository();

        /// <summary>
        /// 判断是否需要提醒的检查器列表
        /// </summary>
        private readonly List<INoteTime> noteCheckList;

        /// <summary>
        /// 所有通知器，比如钉钉通知、短信通知、窗体通知等
        /// </summary>
        private readonly List<INoteAlert> AllAlerts = new List<INoteAlert>();

        /// <summary>
        /// 缓存每个提醒的最近一次提醒时间，重启丢失，重新判断
        /// </summary>
        private static Dictionary<int, DateTime> lastNoteTimes = new Dictionary<int, DateTime>();

        public NotesService()
        {
            noteCheckList = new List<INoteTime>();
            noteCheckList.Add(new NotePerMinute());
            noteCheckList.Add(new NotePerHour());
            noteCheckList.Add(new NotePerDay());
            noteCheckList.Add(new NotePerMonth());
            noteCheckList.Add(new NotePerWeek());
            noteCheckList.Add(new NotePerYear());
            noteCheckList.Add(new NotePerWeekEnd());
            noteCheckList.Add(new NotePerWeekNormal());
            noteCheckList.Add(new NoteSingleTime());

            AllAlerts.Add(new NoteAlertByForm());
            AllAlerts.Add(new NoteAlertByDingDing());
        }

        /// <summary>
        /// 根据id，返回对象
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public Notes FindById(int id)
        {
            return notesRepository.FindById(id);
        }

        /// <summary>
        /// 返回所有存在的对象
        /// </summary>
        /// <returns></returns>
        public List<Notes> FindAll()
        {
            return notesRepository.FindAll();
        }

        /// <summary>
        /// 保存
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public Notes Save(Notes model)
        {
            var ret = notesRepository.Save(model);
            versionRepository.PlusVersion();
            return ret;
        }

        public bool Del(Notes model)
        {
            var ret = notesRepository.Del(model);
            versionRepository.PlusVersion();
            return ret;
        }

        public bool IsNoteTime(int noteId, int indexId, Notes.NoteDetail detail)
        {
            foreach (var noteCheck in noteCheckList)
            {
                if (!noteCheck.Handle(detail.EventType))
                    continue;

                var lastNoteTime = GetLastNoteTime(noteId, indexId);
                if (noteCheck.IsTime(detail.EventTime, lastNoteTime))
                {
                    // 判断通过，要设置本次提醒时间
                    SetLastNoteTime(noteId, indexId);
                    return true;
                }
            }

            return false;
        }

        private DateTime GetLastNoteTime(int noteId, int indexId)
        {
            var key = noteId * 10000 + indexId;
            lock (lastNoteTimes)
            {
                if (lastNoteTimes.TryGetValue(key, out var ret))
                    return ret;
            }

            return DateTime.MinValue;
        }

        // 修改内存里的最近提醒时间，避免重复提醒
        private void SetLastNoteTime(int noteId, int indexId)
        {
            var key = noteId * 10000 + indexId;
            var now = DateTime.Now;
            lock (lastNoteTimes)
            {
                lastNoteTimes[key] = now;
            }
        }


        #region 计划任务扫描方法

        public void ScanAllNote()
        {
            var allNotes = FindAll();
            foreach (var note in allNotes)
            {
                var detailId = 0;
                foreach (var noteDetail in note.Details)
                {
                    if (IsNoteTime(note.Id, detailId, noteDetail))
                    {
                        StartNote(note);
                    }

                    detailId++;
                }
            }
        }

        private void StartNote(Notes note)
        {
            foreach (var noteAlert in AllAlerts)
            {
                // 改用线程进行通知避免阻塞和异常
                ThreadPool.UnsafeQueueUserWorkItem(state =>
                {
                    var tmpNote = (Notes) state;
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

        #endregion
    }
}