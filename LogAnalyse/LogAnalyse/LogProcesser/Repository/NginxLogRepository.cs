using Beinet.Repository.Repositories;

namespace LogAnalyse.LogProcesser.Repository
{
    /// <summary>
    /// 任务表仓储类
    /// </summary>
    public interface NginxLogRepository : JpaRepository<NginxLog, int>
    {
    }
}