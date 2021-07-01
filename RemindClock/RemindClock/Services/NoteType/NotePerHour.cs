using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每小时提醒的判断类
    /// </summary>
    class NotePerHour : INoteTime
    {
        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每小时";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            var now = DateTime.Now;

            // 这小时的这一分钟已经提醒过，忽略
            if (lastNoteTime.Hour == now.Hour && lastNoteTime.Minute == now.Minute)
                return false;

            return now.Minute == eventTime.Minute && now.Second >= eventTime.Second;
        }
    }
}