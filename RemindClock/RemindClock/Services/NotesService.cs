using System.Collections.Generic;
using RemindClock.Repository;
using RemindClock.Repository.Model;

namespace RemindClock.Services
{
    class NotesService
    {
        private NotesRepository notesRepository = new NotesRepository();

        /// <summary>
        /// 根据id，返回对象
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public Notes FindById(int id)
        {
            return notesRepository.FindById(id);
        }

        /// <summary>
        /// 返回所有存在的对象
        /// </summary>
        /// <returns></returns>
        public List<Notes> FindAll()
        {
            return notesRepository.FindAll();
        }

        /// <summary>
        /// 保存
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public Notes Save(Notes model)
        {
            return notesRepository.Save(model);
        }
    }
}