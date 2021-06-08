using System;
using Beinet.Repository.Entitys;

namespace RemindClockWeb.Repository.Model
{
    [Entity]
    public class NoteDetail
    {
        /// <summary>
        /// 数据库自增主键
        /// </summary>
        [Id]
        [GeneratedValue(Strategy = GenerationType.IDENTITY)]
        public int Id { get; set; }

        /// <summary>
        /// 所属提醒的主键
        /// </summary>
        public int NoteId { get; set; }

        /// <summary>
        /// 提醒时间
        /// </summary>
        public DateTime EventTime { get; set; }

        /// <summary>
        /// 提醒类型
        /// "单次","每分钟","每小时","每天","周一~周五每天","周六~周日每天","每周","每月","每年"
        /// </summary>
        public string EventType { get; set; }

        /// <summary>
        ///  入库时间
        /// </summary>
        [Column(Insertable = false, Updatable = false)]
        public DateTime CreationTime { get; set; }

        /// <summary>
        ///  最后编辑时间
        /// </summary>
        [Column(Insertable = false, Updatable = false)]
        public DateTime LastModifyTime { get; set; }
    }
}