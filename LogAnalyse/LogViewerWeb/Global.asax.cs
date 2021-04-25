using System.Web.Http;

namespace LogViewerWeb
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            // Consoller输出 关闭XML支持
            GlobalConfiguration.Configuration?.Formatters?.XmlFormatter?.SupportedMediaTypes?.Clear();

            GlobalConfiguration.Configure(WebApiConfig.Register);
        }
    }
}
