using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每周一到周五提醒的判断类
    /// </summary>
    class NotePerWeekNormal : INoteTime
    {
        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "周一~周五每天";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            var now = DateTime.Now;
            // 今天已经提醒过，忽略
            if (lastNoteTime.Day == now.Day)
                return false;

            return now.DayOfWeek != DayOfWeek.Saturday
                   && now.DayOfWeek != DayOfWeek.Sunday
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}