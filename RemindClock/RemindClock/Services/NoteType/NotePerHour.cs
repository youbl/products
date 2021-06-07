using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每小时提醒的判断类
    /// </summary>
    class NotePerHour : INoteTime
    {
        public DateTime EventTime { get; set; }

        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每小时";
        }

        public bool IsTime(DateTime lastNoteTime)
        {
            // 这小时已经提醒过，忽略
            if (lastNoteTime.Hour == EventTime.Hour)
                return false;

            var now = DateTime.Now;
            return now.Minute == EventTime.Minute && now.Second >= EventTime.Second;
        }
    }
}