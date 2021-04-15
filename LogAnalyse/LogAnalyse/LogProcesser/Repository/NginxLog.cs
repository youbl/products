using System;
using Beinet.Repository.Entitys;
using Newtonsoft.Json;

namespace LogAnalyse.LogProcesser.Repository
{
    /// <summary>
    /// Nginx日志表
    /// </summary>
    [Entity]
    public class NginxLog
    {
        [Id]
        [GeneratedValue(Strategy = GenerationType.IDENTITY)]
        public int Id { get; set; }

        public string Timelocal { get; set; } // '时间',
        public string Remoteaddr { get; set; } // 'ip',
        public string Remoteuser { get; set; } // '用户',
        public string Host { get; set; } // '主机',
        public string Request { get; set; } // '请求方法和地址',
        public int Status { get; set; } // '响应状态',
        public int Requestlength { get; set; } // '请求长度',
        public int Bodybytessent { get; set; } // '发送长度',
        public string Referer { get; set; } // 'referer',
        public string Useragent { get; set; } // 'ua',
        public string Forwardedfor { get; set; } // '代理ip',
        public string Upstreamaddr { get; set; } // '后端ip+端口',
        public int Requesttime { get; set; } // '请求时长',
        public int Upstreamtime { get; set; } // '后端响应时长',
        public int Upstreamstatus { get; set; } // '后端状态',
        public int Contentlength { get; set; } // '内容长度',
        public int Httpcontentlength { get; set; } // 'http内容长',
        public int Sentcontentlength { get; set; } // '发送内容长',
        public string Filename { get; set; } // '采集源文件',

        public override string ToString()
        {
            return JsonConvert.SerializeObject(this);
        }
    }
}