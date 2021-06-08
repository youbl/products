﻿using System;

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
            // 今天已经提醒过，忽略
            if (lastNoteTime.Day == eventTime.Day)
                return false;

            var now = DateTime.Now;
            return now.DayOfWeek == eventTime.DayOfWeek
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}