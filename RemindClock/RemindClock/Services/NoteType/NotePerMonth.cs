using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每月提醒的判断类
    /// </summary>
    class NotePerMonth : INoteTime
    {
        public DateTime EventTime { get; set; }

        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每月";
        }

        public bool IsTime(DateTime lastNoteTime)
        {
            // 本月已经提醒过，忽略
            if (lastNoteTime.Month == EventTime.Month)
                return false;

            var now = DateTime.Now;
            return now.Day == EventTime.Day
                   && now.Hour == EventTime.Hour
                   && now.Minute == EventTime.Minute
                   && now.Second >= EventTime.Second;
        }
    }
}