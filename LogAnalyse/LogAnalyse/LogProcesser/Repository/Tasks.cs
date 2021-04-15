using System;
using Beinet.Repository.Entitys;
using Newtonsoft.Json;

namespace LogAnalyse.LogProcesser.Repository
{
    /// <summary>
    /// 任务表
    /// </summary>
    [Entity]
    public class Tasks
    {
        [Id]
        [GeneratedValue(Strategy = GenerationType.IDENTITY)]
        public int Id { get; set; }

        public string FileName { get; set; }

        public int State { get; set; }

        [Column(Insertable = false, Updatable = false)]
        public DateTime CreationTime { get; set; }

        [Column(Name = "LastModificationTime", Insertable = false, Updatable = false)]
        public DateTime LastTime { get; set; }

        public override string ToString()
        {
            return JsonConvert.SerializeObject(this);
        }
    }
}