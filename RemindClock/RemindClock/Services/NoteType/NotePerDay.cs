using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每天提醒的判断类
    /// </summary>
    class NotePerDay : INoteTime
    {
        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每天";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            // 这天已经提醒过，忽略
            if (lastNoteTime.Day == eventTime.Day)
                return false;

            var now = DateTime.Now;
            return now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}