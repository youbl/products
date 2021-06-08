using System;
using System.Collections.Generic;
using System.Text;
using Beinet.Repository.Entitys;

namespace RemindClockWeb.Repository.Model
{
    /// <summary>
    /// 用户类
    /// </summary>
    [Entity]
    public class Users
    {
        /// <summary>
        /// 数据库自增主键
        /// </summary>
        [Id]
        [GeneratedValue(Strategy = GenerationType.IDENTITY)]
        public int Id { get; set; }
        
        /// <summary>
        ///  账号
        /// </summary>
        public string Account { get; set; }

        /// <summary>
        ///  客户端API调用的Token
        /// </summary>
        public string Token { get; set; }

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