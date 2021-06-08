using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Beinet.Repository;
using RemindClockWeb.Repository;
using RemindClockWeb.Repository.Model;

namespace RemindClockWeb.Services
{
    public class NotesService
    {
        private readonly UsersRepository usersRepository = ProxyLoader.GetProxy<UsersRepository>();
        private readonly NotesRepository notesRepository = ProxyLoader.GetProxy<NotesRepository>();
        private readonly NotesDetailRepository detailRepository = ProxyLoader.GetProxy<NotesDetailRepository>();

        /// <summary>
        /// 根据用户账号和Token，读取所有提醒数据
        /// </summary>
        /// <param name="account">账号</param>
        /// <param name="token">token</param>
        /// <returns>提醒数据</returns>
        public List<Notes> GetNotesByAccount(string account, string token)
        {
            var user = GetUser(account, token);
            return GetNotesByUser(user.Id);
        }

        /// <summary>
        /// 根据用户账号和Token，保存所有提醒数据
        /// </summary>
        /// <param name="account">账号</param>
        /// <param name="token">token</param>
        /// <param name="notes">要保存的提醒数据</param>
        public void SaveNotesByAccount(string account, string token, List<Notes> notes)
        {
            var user = GetUser(account, token);

            // 注意，必须先删除Detail，否则会造成删除失败
            detailRepository.DeleteByUser(user.Id);
            notesRepository.DeleteByUser(user.Id);
            foreach (var note in notes)
            {
                // 上传的Id是客户端标识，不能用
                note.ClientId = note.Id;
                note.Id = 0;
                note.UserId = user.Id;
                notesRepository.Save(note);

                foreach (var detail in note.Details)
                {
                    detail.Id = 0;
                    detail.NoteId = note.Id;
                    detailRepository.Save(detail);
                }
            }
        }

        private List<Notes> GetNotesByUser(int userId)
        {
            var ret = notesRepository.GetByUser(userId);
            foreach (var note in ret)
            {
                note.Details = detailRepository.GetByNoteId(note.Id);
            }

            return ret;
        }

        private Users GetUser(string account, string token)
        {
            var user = usersRepository.GetByAccount(account);
            if (user == null)
            {
                throw new ArgumentException("指定的账户不存在:" + account);
            }

            if (token != user.Token)
            {
                throw new ArgumentException("指定的账户Token无效:" + account);
            }

            return user;
        }
    }
}