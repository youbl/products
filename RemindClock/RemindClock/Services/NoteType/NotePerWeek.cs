using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每周提醒的判断类
    /// </summary>
    class NotePerWeek : INoteTime
    {
        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每周";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            var now = DateTime.Now;
            // 今天已经提醒过，忽略
            if (lastNoteTime.Year == now.Year && lastNoteTime.Month == now.Month && lastNoteTime.Day == now.Day &&
                lastNoteTime.Hour == now.Hour && lastNoteTime.Minute == now.Minute)
                return false;

            return now.DayOfWeek == eventTime.DayOfWeek
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}