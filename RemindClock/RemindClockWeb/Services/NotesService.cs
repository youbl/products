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
        /// <returns>服务端版本号</returns>
        public int SaveNotesByAccount(string account, string token, List<Notes> notes)
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
                note.Title = CutStr(note.Title, 200); // 汉字也按1计算
                note.Content = CutStr(note.Content, 500);

                // note的id不会变化，还是0
                var savedRec = notesRepository.Save(note);

                foreach (var detail in note.Details)
                {
                    detail.Id = 0;
                    detail.NoteId = savedRec.Id;
                    detailRepository.Save(detail);
                }
            }

            if (IncrVersion(user.Id, user.Version))
            {
                return user.Version + 1;
            }

            throw new Exception("版本号加1出错:" + user.Id + ":" + account + " 当前版本:" + user.Version);
        }

        /// <summary>
        /// 查找指定用户的数据版本号
        /// </summary>
        /// <param name="account">账号</param>
        /// <param name="token">token</param>
        /// <returns>版本</returns>
        public int GetVersion(string account, string token)
        {
            return GetUser(account, token).Version;
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

        private bool IncrVersion(int userId, int oldVersion)
        {
            return usersRepository.IncrVersion(userId, oldVersion) > 0;
        }

        private string CutStr(string str, int len)
        {
            if (str.Length > len)
                return str.Substring(0, len);
            return str;
        }
    }
}