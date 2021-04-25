using System;
using Beinet.Repository.Entitys;

namespace LogViewerWeb.Services.Repository
{
    /// <summary>
    /// Nginx日志统计表
    /// </summary>
    [Entity]
    public class NginxAppLog
    {
        [Id]
        [GeneratedValue(Strategy = GenerationType.IDENTITY)]
        public int Id { get; set; }

        /// <summary>
        /// 数据统计所在的小时，如2021042512,表示4月25号12点
        /// </summary>
        public int Hour { get; set; }

        /// <summary>
        /// 是否前端访问，通过有没有Refer判断
        /// </summary>
        public int IsFront { get; set; }

        /// <summary>
        /// 请求的app名称
        /// </summary>
        public string App { get; set; }

        /// <summary>
        /// 访问次数
        /// </summary>
        public int Num { get; set; }

        /// <summary>
        /// 数据所在的nginx日志文件
        /// </summary>
        public int Day { get; set; }

        [Column(Insertable = false, Updatable = false)]
        public DateTime CreationTime { get; set; }
    }
}