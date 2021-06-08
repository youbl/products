using System.Collections.Generic;
using System.Web.Http;
using RemindClockWeb.Repository.Model;
using RemindClockWeb.Services;

namespace RemindClockWeb.Controllers
{
    public class DefaultController : ApiController
    {
        private readonly NotesService notesService = new NotesService();

        // GET api/<controller>
        [Route("notes"), HttpGet]
        public List<Notes> GetNotes()
        {
            return null;//ew string[] {"value1", "value2"};
        }

        // GET api/<controller>/5
        public string Get(int id)
        {
            return "value";
        }

        // POST api/<controller>
        public void Post([FromBody] string value)
        {
        }

        // PUT api/<controller>/5
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/<controller>/5
        public void Delete(int id)
        {
        }
    }
}