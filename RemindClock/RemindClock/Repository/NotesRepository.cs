using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using RemindClock.Repository.Model;
using RemindClock.Utils;

namespace RemindClock.Repository
{
    /// <summary>
    /// 记事类仓储层
    /// </summary>
    public class NotesRepository : BaseRepository<Notes>
    {
        /// <summary>
        /// 同步前先备份
        /// </summary>
        public void BackupAll()
        {
            var bakName = ModelDir + DateTime.Now.ToString("yyyyMMddHHmmss");
            Directory.Move(ModelDir, bakName);
            FileHelper.CreateDir(ModelDir);
        }

        public List<Notes> FindAllEnabled()
        {
            var allNotes = FindAll();
            return allNotes.Where(item => item.Enable).ToList();
        }
    }
}