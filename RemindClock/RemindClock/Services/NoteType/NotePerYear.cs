using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每年提醒的判断类
    /// </summary>
    class NotePerYear : INoteTime
    {
        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每年";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            // 今年已经提醒过，忽略
            if (lastNoteTime.Year == eventTime.Year)
                return false;

            var now = DateTime.Now;
            return now.Month == eventTime.Month
                   && now.Day == eventTime.Day
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}