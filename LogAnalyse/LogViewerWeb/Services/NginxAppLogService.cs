using System;
using System.Collections.Generic;
using System.Linq;
using Beinet.Repository;
using LogViewerWeb.Services.Repository;

namespace LogViewerWeb.Services
{
    public class NginxAppLogService
    {
        private NginxAppLogRepository nginxAppLogRepository = ProxyLoader.GetProxy<NginxAppLogRepository>();
        private ConfigsRepository configsRepository = ProxyLoader.GetProxy<ConfigsRepository>();

        /// <summary>
        /// 按小时汇总app数据
        /// </summary>
        /// <param name="app"></param>
        /// <param name="start"></param>
        /// <param name="end"></param>
        /// <returns></returns>
        public List<NginxAppLog> GetAppGroupDataByHour(string app, int start, int end)
        {
            if (string.IsNullOrEmpty(app))
                return nginxAppLogRepository.GroupByAppAndHour(start, end);
            return nginxAppLogRepository.GroupByAppAndHour(SplitApp(app), start, end);
        }


        /// <summary>
        /// 按天汇总app数据
        /// </summary>
        /// <param name="app"></param>
        /// <param name="start"></param>
        /// <param name="end"></param>
        /// <returns></returns>
        public List<NginxAppLog> GetAppGroupDataByDay(string app, int start, int end)
        {
            if (string.IsNullOrEmpty(app))
                return nginxAppLogRepository.GroupByAppAndDay(start, end);
            return nginxAppLogRepository.GroupByAppAndDay(SplitApp(app), start, end);
        }

        public List<String> GetAppList()
        {
            return configsRepository.findAllVal("app");
        }

        List<string> SplitApp(string app)
        {
            return app.Split(new[] {','}, StringSplitOptions.RemoveEmptyEntries).ToList();
        }
    }
}