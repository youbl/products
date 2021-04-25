using System;
using Beinet.Repository.Entitys;
using NLog;

namespace LogAnalyse.LogProcesser.Repository
{
    /// <summary>
    /// 配置表
    /// </summary>
    [Entity]
    public class Configs
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

        [Id]
        [GeneratedValue(Strategy = GenerationType.IDENTITY)]
        public int Id { get; set; }

        public string Type { get; set; }

        public string Val { get; set; }

        [Column(Insertable = false, Updatable = false)]
        public DateTime CreationTime { get; set; }
    }
}