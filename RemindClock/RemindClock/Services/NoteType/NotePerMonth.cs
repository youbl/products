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
            var now = DateTime.Now;
            // 本月这天的这分钟已经提醒过，忽略
            if (lastNoteTime.Month == now.Month && lastNoteTime.Day == now.Day && lastNoteTime.Hour == now.Hour &&
                lastNoteTime.Minute == now.Minute)
                return false;

            return now.Day == eventTime.Day
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}