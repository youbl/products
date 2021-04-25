using System.Web.Http;

namespace LogViewerWeb
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            // Consoller��� �ر�XML֧��
            GlobalConfiguration.Configuration?.Formatters?.XmlFormatter?.SupportedMediaTypes?.Clear();

            GlobalConfiguration.Configure(WebApiConfig.Register);
        }
    }
}
