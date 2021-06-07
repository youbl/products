using System;

namespace RemindClock.Services.NoteType
{
    interface INoteTime
    {
        /// <summary>
        /// 指定的类型是否可以处理
        /// </summary>
        /// <param name="eventType">触发类型，如每分钟</param>
        /// <returns></returns>
        bool Handle(string eventType);

        /// <summary>
        /// 触发时间配置
        /// </summary>
        DateTime EventTime { set; }

        /// <summary>
        /// 当前时间是否需要提醒
        /// </summary>
        /// <param name="lastNoteTime">上次提醒时间，用于判断是否可以重复提醒</param>
        /// <returns></returns>
        bool IsTime(DateTime lastNoteTime);
    }
}