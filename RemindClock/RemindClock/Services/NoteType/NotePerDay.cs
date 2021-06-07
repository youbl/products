using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每天提醒的判断类
    /// </summary>
    class NotePerDay : INoteTime
    {
        public DateTime EventTime { get; set; }

        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每天";
        }

        public bool IsTime(DateTime lastNoteTime)
        {
            // 这天已经提醒过，忽略
            if (lastNoteTime.Day == EventTime.Day)
                return false;

            var now = DateTime.Now;
            return now.Hour == EventTime.Hour
                   && now.Minute == EventTime.Minute
                   && now.Second >= EventTime.Second;
        }
    }
}