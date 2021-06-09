using System;
using System.Collections.Generic;
using System.Web.Http;
using RemindClockWeb.Repository.Model;
using RemindClockWeb.Services;

namespace RemindClockWeb.Controllers
{
    public class DefaultController : ApiController
    {
        private readonly NotesService notesService = new NotesService();

        /// <summary>
        /// 根据用户账号返回所有提醒数据
        /// </summary>
        /// <param name="account"></param>
        /// <param name="token"></param>
        /// <returns></returns>
        [Route("notes"), HttpGet]
        public List<Notes> GetNotes([FromUri] string account, [FromUri] string token)
        {
            if (string.IsNullOrEmpty(account) || string.IsNullOrEmpty(token))
            {
                throw new ArgumentException("account和token不能为空");
            }

            return notesService.GetNotesByAccount(account, token);
        }

        /// <summary>
        /// 把客户端的数据同步到服务端保存，并返回服务端版本号
        /// </summary>
        /// <param name="account"></param>
        /// <param name="token"></param>
        /// <param name="notes"></param>
        /// <returns></returns>
        [Route("notes"), HttpPost]
        public int SaveNotes([FromUri] string account, [FromUri] string token, [FromBody] List<Notes> notes)
        {
            if (string.IsNullOrEmpty(account) || string.IsNullOrEmpty(token) || notes == null)
            {
                throw new ArgumentException("account和token、notes均不能为空");
            }

            return notesService.SaveNotesByAccount(account, token, notes);
        }

        /// <summary>
        /// 读取服务端版本号，以便客户端判断同步策略
        /// </summary>
        /// <param name="account"></param>
        /// <param name="token"></param>
        /// <returns></returns>
        [Route("version"), HttpGet]
        public int GetVersion([FromUri] string account, [FromUri] string token)
        {
            if (string.IsNullOrEmpty(account) || string.IsNullOrEmpty(token))
            {
                throw new ArgumentException("account和token均不能为空");
            }

            return notesService.GetVersion(account, token);
        }
    }
}