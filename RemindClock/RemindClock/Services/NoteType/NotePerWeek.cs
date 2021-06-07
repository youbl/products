using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每周提醒的判断类
    /// </summary>
    class NotePerWeek : INoteTime
    {
        public DateTime EventTime { get; set; }

        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每周";
        }

        public bool IsTime(DateTime lastNoteTime)
        {
            // 今天已经提醒过，忽略
            if (lastNoteTime.Day == EventTime.Day)
                return false;

            var now = DateTime.Now;
            return now.DayOfWeek == EventTime.DayOfWeek
                   && now.Hour == EventTime.Hour
                   && now.Minute == EventTime.Minute
                   && now.Second >= EventTime.Second;
        }
    }
}