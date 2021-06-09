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
            logger.Info("վ������");

            GlobalConfiguration.Configure(WebApiConfig.Register);

            // ��ֹxml��ʽ֧��
            GlobalConfiguration.Configuration.Formatters.XmlFormatter.SupportedMediaTypes.Clear();

            // ȫ���쳣����
            GlobalConfiguration.Configuration.Filters.Add(new WebApiExceptionFilterAttribute());
        }

        protected void Application_Error(object sender, EventArgs e)
        {
            Exception ex = Server.GetLastError().InnerException ?? Server.GetLastError();
            if (ex is ThreadAbortException)
            {
                // ����¼Response.End�������쳣
                Thread.ResetAbort();
                HttpContext.Current.ClearError();
                return;
            }

            logger.Error(ex, "Global����:");
        }
    }
}