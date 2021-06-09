using System.Web.Http;
using RemindClockWeb.Controllers;

namespace RemindClockWeb
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            GlobalConfiguration.Configure(WebApiConfig.Register);

            // ��ֹxml��ʽ֧��
            GlobalConfiguration.Configuration.Formatters.XmlFormatter.SupportedMediaTypes.Clear();

            // ȫ���쳣����
            GlobalConfiguration.Configuration.Filters.Add(new WebApiExceptionFilterAttribute());
        }
    }
}