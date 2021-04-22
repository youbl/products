using System;
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

        public void Parse(NginxLog ngingLog)
        {
            try
            {
                nginxLogRepository.Save(ngingLog);
            }
            catch (Exception exp)
            {
                logger.Error(ngingLog.Filename + " " + exp);
            }
        }

        public void Finish(string day)
        {
        }
    }
}