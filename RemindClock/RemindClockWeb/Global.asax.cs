using System.Web.Http;
using RemindClockWeb.Controllers;

namespace RemindClockWeb
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            GlobalConfiguration.Configure(WebApiConfig.Register);

            // 禁止xml格式支持
            GlobalConfiguration.Configuration.Formatters.XmlFormatter.SupportedMediaTypes.Clear();

            // 全局异常处理
            GlobalConfiguration.Configuration.Filters.Add(new WebApiExceptionFilterAttribute());
        }
    }
}