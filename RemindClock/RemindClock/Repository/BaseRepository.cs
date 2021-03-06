﻿using System;
using System.Collections.Generic;
using System.IO;
using Beinet.Core.Serializer;
using RemindClock.Repository.Model;
using RemindClock.Utils;

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

        /// <summary>
        /// 当前模型保存的目录
        /// </summary>
        protected string ModelDir { get; private set; }

        protected BaseRepository()
        {
            this.ModelDir = Path.Combine(baseDir, typeof(T).Name);
            FileHelper.CreateDir(this.ModelDir);
        }

        /// <summary>
        /// 根据id，返回对象
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public T FindById(int id)
        {
            if (id <= 0)
                return null;
            var fileName = Path.Combine(this.ModelDir, id + fileExt);
            if (!File.Exists(fileName))
                return null;
            return serializer.DeSerializFromFile<T>(fileName);
        }

        /// <summary>
        /// 返回所有存在的对象
        /// </summary>
        /// <returns></returns>
        public List<T> FindAll()
        {
            var ret = new List<T>();
            foreach (var file in Directory.GetFiles(this.ModelDir, "*" + fileExt, SearchOption.TopDirectoryOnly))
            {
                if (int.TryParse(Path.GetFileNameWithoutExtension(file), out var id))
                {
                    var item = serializer.DeSerializFromFile<T>(file);
                    item.Id = id; // 避免复制导致ID有问题的情况
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

        public bool Del(T model)
        {
            var file = Path.Combine(this.ModelDir, model.Id.ToString() + fileExt);
            if (File.Exists(file))
            {
                File.Delete(file);
                return true;
            }

            return false;
        }

        private void SerializeToFile(T obj)
        {
            var file = Path.Combine(this.ModelDir, obj.Id.ToString() + fileExt);
            serializer.SerializToFile(file, obj);
        }

        private int FindMaxId()
        {
            int maxId = 0;
            foreach (var file in Directory.GetFiles(this.ModelDir, "*" + fileExt, SearchOption.TopDirectoryOnly))
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