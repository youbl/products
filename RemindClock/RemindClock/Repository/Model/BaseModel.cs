using System;

namespace RemindClock.Repository.Model
{
    public abstract class BaseModel
    {
        public int Id { get; set; }

        /// <summary>
        ///  注册时间
        /// </summary>
        public DateTime CreationTime { get; set; }

        /// <summary>
        ///  最后编辑时间
        /// </summary>
        public DateTime LastModifyTime { get; set; }
    }
}