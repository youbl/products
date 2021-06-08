using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每月提醒的判断类
    /// </summary>
    class NotePerMonth : INoteTime
    {
        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每月";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            // 本月已经提醒过，忽略
            if (lastNoteTime.Month == eventTime.Month)
                return false;

            var now = DateTime.Now;
            return now.Day == eventTime.Day
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}