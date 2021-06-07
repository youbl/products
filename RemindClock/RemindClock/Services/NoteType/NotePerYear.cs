using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每年提醒的判断类
    /// </summary>
    class NotePerYear : INoteTime
    {
        public DateTime EventTime { get; set; }

        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每年";
        }

        public bool IsTime(DateTime lastNoteTime)
        {
            // 今年已经提醒过，忽略
            if (lastNoteTime.Year == EventTime.Year)
                return false;

            var now = DateTime.Now;
            return now.Month == EventTime.Month
                   && now.Day == EventTime.Day
                   && now.Hour == EventTime.Hour
                   && now.Minute == EventTime.Minute
                   && now.Second >= EventTime.Second;
        }
    }
}