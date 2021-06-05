using System;
using System.Collections.Generic;
using System.IO;
using Beinet.Core.Serializer;
using RemindClock.Repository.Model;

namespace RemindClock.Repository
{
    /// <summary>
    /// 仓储层基类
    /// </summary>
    public abstract class BaseRepository<T> where T : BaseModel
    {
        private static readonly JsonSerializer serializer = new JsonSerializer();

        private static readonly string baseDir = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "data");
        private const string fileExt = ".data";
        private string modelDir;

        protected BaseRepository()
        {
            this.modelDir = Path.Combine(baseDir, typeof(T).Name);
            if (!Directory.Exists(this.modelDir))
            {
                Directory.CreateDirectory(this.modelDir);
            }
        }

        /// <summary>
        /// 返回所有存在的对象
        /// </summary>
        /// <returns></returns>
        public List<T> FindAll()
        {
            var ret = new List<T>();
            foreach (var file in Directory.GetFiles(this.modelDir, "*" + fileExt, SearchOption.TopDirectoryOnly))
            {
                if (int.TryParse(Path.GetFileNameWithoutExtension(file), out _))
                {
                    var item = serializer.DeSerializFromFile<T>(file);
                    ret.Add(item);
                }
            }

            return ret;
        }

        /// <summary>
        /// 保存
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        public T Save(T model)
        {
            var now = DateTime.Now;
            if (model.Id <= 0)
            {
                // Insert
                model.Id = FindMaxId() + 1;
                model.CreationTime = now;
            }

            model.LastModifyTime = now;
            SerializeToFile(model);
            return model;
        }

        private void SerializeToFile(T obj)
        {
            var file = Path.Combine(this.modelDir, obj.Id.ToString() + fileExt);
            serializer.SerializToFile(file, obj);
        }

        private int FindMaxId()
        {
            int maxId = 0;
            foreach (var file in Directory.GetFiles(this.modelDir, "*" + fileExt, SearchOption.TopDirectoryOnly))
            {
                if (int.TryParse(Path.GetFileNameWithoutExtension(file), out var id) && id > maxId)
                {
                    maxId = id;
                }
            }

            return maxId + 1;
        }
    }
}