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
        public List<NoteDetail> Details { get; set; }

        public class NoteDetail
        {
            /// <summary>
            /// 提醒类型, 支持2种：late:相对时间，只提醒一次，如60s; time:绝对时间只提醒一次、cron:Cron表达式
            /// </summary>
            public string EventType { get; set; }

            public string EventArg { get; set; }
        }
    }
}