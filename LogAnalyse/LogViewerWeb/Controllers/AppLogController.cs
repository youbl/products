using System;
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
        public List<NginxAppLog> GetAppLogByHour(string app, int start, int end, int front, int weekend)
        {
            var ret = nginxAppLogService.GetAppGroupDataByHour(app, start, end, front);
            FilterByWeekCondition(ret, weekend);
            return ret;
        }

        [HttpGet]
        [Route("byDay")]
        public List<NginxAppLog> GetAppLogByDay(string app, int start, int end, int front, int weekend)
        {
            var ret = nginxAppLogService.GetAppGroupDataByDay(app, start, end, front);
            FilterByWeekCondition(ret, weekend);
            return ret;
        }

        // 根据是否周末条件进行过滤
        void FilterByWeekCondition(List<NginxAppLog> logs, int weekend)
        {
            if (weekend == 2)
                return;

            for (var i = logs.Count - 1; i >= 0; i--)
            {
                var dt = ConvertToDT(logs[i].Hour);
                var week = dt.DayOfWeek;
                // 只要周末
                if (weekend == 1 && week != DayOfWeek.Saturday && week != DayOfWeek.Sunday)
                {
                    logs.RemoveAt(i);
                    continue;
                }

                // 只要平日
                if (weekend == 0 && (week == DayOfWeek.Saturday || week == DayOfWeek.Sunday))
                {
                    logs.RemoveAt(i);
                }
            }
        }

        DateTime ConvertToDT(int time)
        {
            int year, month, day, hour;
            if (time > 99991231)
            {
                // 10位的小时维度
                year = time / 1000000;
                month = (time % 1000000) / 10000;
                day = (time % 10000) / 100;
                hour = time % 100;
            }
            else
            {
                // 8位的日期维度
                year = time / 10000;
                month = (time % 10000) / 100;
                day = (time % 100);
                hour = 0;
            }

            return new DateTime(year, month, day, hour, 0, 0);
        }
    }
}