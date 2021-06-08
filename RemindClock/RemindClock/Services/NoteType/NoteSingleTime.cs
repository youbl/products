using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 单次提醒的判断类
    /// </summary>
    class NoteSingleTime : INoteTime
    {
        public bool Handle(string eventType)
        {
            return string.IsNullOrEmpty(eventType) || eventType == "单次";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            // 已经提醒过1次，忽略
            if (lastNoteTime > DateTime.MinValue)
                return false;

            var now = DateTime.Now;
            return now.Year == eventTime.Year
                   && now.Month == eventTime.Month
                   && now.Day == eventTime.Day
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}