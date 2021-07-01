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
            var now = DateTime.Now;
            if (lastNoteTime.Year == now.Year && lastNoteTime.Month == now.Month && lastNoteTime.Day == now.Day &&
                lastNoteTime.Hour == now.Hour && lastNoteTime.Minute == now.Minute)
                return false;

            return now.Year == eventTime.Year
                   && now.Month == eventTime.Month
                   && now.Day == eventTime.Day
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}