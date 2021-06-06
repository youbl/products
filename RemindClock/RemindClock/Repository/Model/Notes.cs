using System;
using System.Collections.Generic;

namespace RemindClock.Repository.Model
{
    /// <summary>
    /// 记事类
    /// </summary>
    public class Notes : BaseModel
    {
        /// <summary>
        ///  标题
        /// </summary>
        public string Title { get; set; }

        /// <summary>
        ///  内容
        /// </summary>
        public string Content { get; set; }

        /// <summary>
        /// 记事提醒明细
        /// </summary>
        public List<NoteDetail> Details { get; set; } = new List<NoteDetail>();

        public class NoteDetail
        {
            /// <summary>
            /// 提醒时间
            /// </summary>
            public DateTime EventTime { get; set; }

            /// <summary>
            /// 提醒类型
            /// "单次","每分钟","每小时","每天","周一~周五每天","周六~周日每天","每周","每月","每年"
            /// </summary>
            public string EventType { get; set; }
        }
    }
}