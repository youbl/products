using System;
using System.Threading;
using System.Web;
using System.Web.Http;
using NLog;
using RemindClockWeb.Controllers;

namespace RemindClockWeb
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        private static readonly ILogger logger = LogManager.GetCurrentClassLogger();

        protected void Application_Start()
        {
            logger.Info("站点启动");

            GlobalConfiguration.Configure(WebApiConfig.Register);

            // 禁止xml格式支持
            GlobalConfiguration.Configuration.Formatters.XmlFormatter.SupportedMediaTypes.Clear();

            // 全局异常处理
            GlobalConfiguration.Configuration.Filters.Add(new WebApiExceptionFilterAttribute());
        }

        protected void Application_Error(object sender, EventArgs e)
        {
            Exception ex = Server.GetLastError().InnerException ?? Server.GetLastError();
            if (ex is ThreadAbortException)
            {
                // 不记录Response.End引发的异常
                Thread.ResetAbort();
                HttpContext.Current.ClearError();
                return;
            }

            logger.Error(ex, "Global错误:");
        }
    }
}