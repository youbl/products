using System.Collections.Generic;
using Beinet.Repository.Repositories;

namespace LogAnalyse.LogProcesser.Repository
{
    /// <summary>
    /// 任务表仓储类
    /// </summary>
    public interface TasksRepository : JpaRepository<Tasks, int>
    {
        [Query("SELECT fileName FROM #{#entityName}")]
        List<string> findAllFileName();
    }
}