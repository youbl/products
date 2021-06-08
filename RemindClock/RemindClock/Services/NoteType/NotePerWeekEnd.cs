﻿using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每周六日提醒的判断类
    /// </summary>
    class NotePerWeekEnd : INoteTime
    {
        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "周六~周日每天";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            var now = DateTime.Now;
            // 今天已经提醒过，忽略
            if (lastNoteTime.Day == now.Day)
                return false;

            return (now.DayOfWeek == DayOfWeek.Saturday || now.DayOfWeek == DayOfWeek.Sunday)
                   && now.Hour == eventTime.Hour
                   && now.Minute == eventTime.Minute
                   && now.Second >= eventTime.Second;
        }
    }
}