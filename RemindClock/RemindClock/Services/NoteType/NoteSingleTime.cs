using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 单次提醒的判断类
    /// </summary>
    class NoteSingleTime : INoteTime
    {
        public DateTime EventTime { get; set; }

        public bool Handle(string eventType)
        {
            return string.IsNullOrEmpty(eventType) || eventType == "单次";
        }

        public bool IsTime(DateTime lastNoteTime)
        {
            // 已经提醒过1次，忽略
            if (lastNoteTime > DateTime.MinValue)
                return false;

            var now = DateTime.Now;
            return now.Year == EventTime.Year
                   && now.Month == EventTime.Month
                   && now.Day == EventTime.Day
                   && now.Hour == EventTime.Hour
                   && now.Minute == EventTime.Minute
                   && now.Second >= EventTime.Second;
        }
    }
}