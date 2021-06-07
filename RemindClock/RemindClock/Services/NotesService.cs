using System;
using System.Collections.Generic;
using RemindClock.Repository;
using RemindClock.Repository.Model;
using RemindClock.Services.NoteType;

namespace RemindClock.Services
{
    class NotesService
    {
        private NotesRepository notesRepository = new NotesRepository();

        /// <summary>
        /// 判断是否需要提醒的检查器列表
        /// </summary>
        private List<INoteTime> noteCheckList;

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
            return notesRepository.Save(model);
        }

        public bool IsNoteTime(int noteId, int indexId, Notes.NoteDetail detail)
        {
            foreach (var noteCheck in noteCheckList)
            {
                var lastNoteTime = GetLastNoteTime(noteId, indexId);
                if (noteCheck.Handle(detail.EventType) && noteCheck.IsTime(lastNoteTime))
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

        private void SetLastNoteTime(int noteId, int indexId)
        {
            var key = noteId * 10000 + indexId;
            var now = DateTime.Now;
            lock (lastNoteTimes)
            {
                lastNoteTimes[key] = now;
            }
        }
    }
}