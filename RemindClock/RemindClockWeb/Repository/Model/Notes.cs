using System;
using System.Collections.Generic;
using System.Text;
using Beinet.Repository.Entitys;

namespace RemindClockWeb.Repository.Model
{
    /// <summary>
    /// 记事类
    /// </summary>
    [Entity]
    public class Notes
    {
        /// <summary>
        /// 数据库自增主键
        /// </summary>
        [Id]
        [GeneratedValue(Strategy = GenerationType.IDENTITY)]
        public int Id { get; set; }

        /// <summary>
        /// 所属用户Id
        /// </summary>
        public int UserId { get; set; }

        /// <summary>
        /// 客户端的主键
        /// </summary>
        public int ClientId { get; set; }

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