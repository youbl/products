using System.Collections.Generic;
using Beinet.Repository;
using LogViewerWeb.Services.Repository;

namespace LogViewerWeb.Services
{
    public class NginxAppLogService
    {
        private static NginxAppLogRepository nginxAppLogRepository = ProxyLoader.GetProxy<NginxAppLogRepository>();

        public List<int> GetAppGroupData(string app)
        {
            return null;
        }
    }
}