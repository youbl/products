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
        public List<NginxAppLog> GetAppGroupDataByHour(string app, int start, int end, int front)
        {
            if (string.IsNullOrEmpty(app))
                return nginxAppLogRepository.GroupByAppAndHour(start, end, front);
            return nginxAppLogRepository.GroupByAppAndHour(SplitApp(app), start, end, front);
        }


        /// <summary>
        /// 按天汇总app数据
        /// </summary>
        public List<NginxAppLog> GetAppGroupDataByDay(string app, int start, int end, int front)
        {
            if (string.IsNullOrEmpty(app))
                return nginxAppLogRepository.GroupByAppAndDay(start, end, front);
            return nginxAppLogRepository.GroupByAppAndDay(SplitApp(app), start, end, front);
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