using Beinet.Core.Cron;
using NLog;

namespace RemindClock.Services
{
    class SchedueService
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();


        /// <summary>
        /// cron格式参考： https://github.com/youbl/DemoCode
        /// 表达式由6个或7个由空格分隔的字符串组成，第7个年可选
        /// </summary>
        [Scheduled(cron: "* * * * * *")]
        public void Begin()
        {
            logger.Info("haha");
        }
    }
}