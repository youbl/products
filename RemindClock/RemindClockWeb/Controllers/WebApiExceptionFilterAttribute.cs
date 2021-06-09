using System.Net;
using System.Net.Http;
using System.Text;
using System.Web.Http.Filters;
using NLog;

namespace RemindClockWeb.Controllers
{
    public class WebApiExceptionFilterAttribute : ExceptionFilterAttribute
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

        public override void OnException(HttpActionExecutedContext actionExecutedContext)
        {
            var exp = actionExecutedContext.Exception;
            logger.Error(exp, " 全局异常:");
            actionExecutedContext.Response = new HttpResponseMessage(HttpStatusCode.InternalServerError)
            {
                Content = new StringContent('"' + exp.Message + '"', Encoding.UTF8, "application/json")
            };

            base.OnException(actionExecutedContext);
        }
    }
}