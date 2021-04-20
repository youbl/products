using System;
using System.Collections.Generic;
using Beinet.Repository;
using LogAnalyse.LogProcesser.Repository;
using NLog;

namespace LogAnalyse.LogProcesser.Parsers
{
    /// <summary>
    /// 插入单行的解析器
    /// </summary>
    class InsertParser : IParser
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

        // Nginx日志仓储类
        private readonly NginxLogRepository nginxLogRepository = ProxyLoader.GetProxy<NginxLogRepository>();

        public void Parse(List<string> arrFields, string fileName)
        {
            try
            {
                var log = new NginxLog();
                int tmp;
                double tmpd;

                log.Timelocal = arrFields[0]; // '时间',
                log.Remoteaddr = arrFields[1]; // 'ip',
                log.Remoteuser = arrFields[2]; // '用户',
                log.Host = arrFields[3]; // '主机',
                log.Request = arrFields[4]; // '请求方法和地址',

                int.TryParse(arrFields[5], out tmp);
                log.Status = tmp; // '响应状态',
                int.TryParse(arrFields[6], out tmp);
                log.Requestlength = tmp; // '请求长度',
                int.TryParse(arrFields[7], out tmp);
                log.Bodybytessent = tmp; // '发送长度',
                log.Referer = arrFields[8]; // 'referer',
                log.Useragent = arrFields[9]; // 'ua',
                log.Forwardedfor = arrFields[10]; // '代理ip',
                log.Upstreamaddr = arrFields[11]; // '后端ip+端口',
                double.TryParse(arrFields[12], out tmpd);
                log.Requesttime = (int) Math.Floor(tmpd * 1000); // '请求时长',
                double.TryParse(arrFields[13], out tmpd);
                log.Upstreamtime = (int) Math.Floor(tmpd * 1000); // '后端响应时长',
                int.TryParse(arrFields[14], out tmp);
                log.Upstreamstatus = tmp; // '后端状态',
                int.TryParse(arrFields[15], out tmp);
                log.Contentlength = tmp; // '内容长度',
                int.TryParse(arrFields[16], out tmp);
                log.Httpcontentlength = tmp; // 'http内容长',
                int.TryParse(arrFields[17], out tmp);
                log.Sentcontentlength = tmp; // '发送内容长',
                log.Filename = fileName; // '采集源文件',

                nginxLogRepository.Save(log);
            }
            catch (Exception exp)
            {
                logger.Error(fileName + " " + exp);
            }
        }
    }
}