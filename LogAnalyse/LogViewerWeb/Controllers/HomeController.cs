using System.Web.Http;

namespace LogViewerWeb.Controllers
{
    public class HomeController : ApiController
    {
        // GET api/<controller>/5
        [HttpGet]
        [Route("home")]
        public string Get(int id)
        {
            return "value";
        }
    }
}