using System;
using System.Collections.Generic;
using System.Text;

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
        ///  钉钉通知时，群机器人的Token.
        /// 为空不通知
        /// </summary>
        public string DingDingToken { get; set; }

        /// <summary>
        /// 是否启用
        /// </summary>
        public bool Enable { get; set; } = true;

        /// <summary>
        /// 记事提醒明细
        /// </summary>
        public List<NoteDetail> Details { get; set; } = new List<NoteDetail>();

        public string GetStrDetail()
        {
            var ret = new StringBuilder();
            foreach (var detail in Details)
            {
                ret.Append(detail).Append(";");
            }

            return ret.ToString();
        }

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

            public override string ToString()
            {
                if (string.IsNullOrEmpty(EventType))
                    EventType = "单次";

                switch (EventType)
                {
                    case "每分钟":
                        return EventType + ":" + EventTime.ToString("ss秒");
                    case "每小时":
                        return EventType + ":" + EventTime.ToString("mm分ss秒");
                    case "每天":
                        return EventType + ":" + EventTime.ToString("HH:mm:ss");
                    case "周一~周五每天":
                        return EventType + ":" + EventTime.ToString("HH:mm:ss");
                    case "周六~周日每天":
                        return EventType + ":" + EventTime.ToString("HH:mm:ss");
                    case "每周":
                        var week = GetChineseWeek(EventTime.DayOfWeek);
                        return EventType + week + EventTime.ToString("HH:mm:ss");
                    case "每月":
                        return EventType + ":" + EventTime.ToString("dd日HH:mm:ss");
                    case "每年":
                        return EventType + ":" + EventTime.ToString("MM月dd日HH:mm:ss");
                    case "单次":
                        return EventType + ":" + EventTime.ToString("yyyy-MM-dd HH:mm:ss");
                }

                return EventTime.ToString("yyyy-MM-dd HH:mm:ss");
            }

            private string GetChineseWeek(DayOfWeek day)
            {
                switch (day)
                {
                    case DayOfWeek.Monday:
                        return "一";
                    case DayOfWeek.Tuesday:
                        return "二";
                    case DayOfWeek.Wednesday:
                        return "三";
                    case DayOfWeek.Thursday:
                        return "四";
                    case DayOfWeek.Friday:
                        return "五";
                    case DayOfWeek.Saturday:
                        return "六";
                    case DayOfWeek.Sunday:
                        return "日";
                }

                return "X";
            }
        }
    }
}