using System;
using Beinet.Core.Cron;
using NLog;

namespace RemindClock.Services
{
    class SchedueService
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();
        private static ShowAlertForm showAlertService = new ShowAlertForm();

        /// <summary>
        /// cron格式参考： https://github.com/youbl/DemoCode
        /// 表达式由6个或7个由空格分隔的字符串组成，第7个年可选
        /// </summary>
        [Scheduled(cron: "0 * * * * *")]
        public void Begin()
        {
            logger.Info("haha");
            showAlertService.Show(DateTime.Now.Second);
        }
    }
}