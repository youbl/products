﻿using System;

namespace RemindClock.Services.NoteType
{
    /// <summary>
    /// 每分钟提醒的判断类
    /// </summary>
    class NotePerMinute : INoteTime
    {
        public bool Handle(string eventType)
        {
            return eventType != null && eventType == "每分钟";
        }

        public bool IsTime(DateTime eventTime, DateTime lastNoteTime)
        {
            // 这分钟已经提醒过，忽略
            if (lastNoteTime.Minute == eventTime.Minute)
                return false;

            var now = DateTime.Now;
            // 避免计时器不精确，导致跳过了1秒
            return now.Second >= eventTime.Second;
        }
    }
}