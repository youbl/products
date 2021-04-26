using System.Collections.Generic;
using System.Web.Http;
using LogViewerWeb.Services;
using LogViewerWeb.Services.Repository;

namespace LogViewerWeb.Controllers
{
    [RoutePrefix("appLog")]
    public class AppLogController : ApiController
    {
        private NginxAppLogService nginxAppLogService = new NginxAppLogService();

        [HttpGet]
        [Route("apps")]
        public List<string> GetAppList()
        {
            return nginxAppLogService.GetAppList();
        }

        [HttpGet]
        [Route("byHour")]
        public List<NginxAppLog> GetAppLogByHour(string app, int start, int end, int front)
        {
            var ret = nginxAppLogService.GetAppGroupDataByHour(app, start, end, front);
            return ret;
        }

        [HttpGet]
        [Route("byDay")]
        public List<NginxAppLog> GetAppLogByDay(string app, int start, int end, int front)
        {
            return nginxAppLogService.GetAppGroupDataByDay(app, start, end, front);
        }
    }
}