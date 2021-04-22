using System.Collections.Generic;
using Beinet.Repository.Repositories;

namespace LogAnalyse.LogProcesser.Repository
{
    /// <summary>
    /// 任务表仓储类
    /// </summary>
    public interface TasksRepository : JpaRepository<Tasks, int>
    {
        [Query("SELECT fileName FROM #{#entityName} where ymd=?1")]
        List<string> findAllFileName(string ymd);
    }
}