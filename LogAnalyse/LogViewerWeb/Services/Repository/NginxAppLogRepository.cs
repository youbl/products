using Beinet.Repository.Repositories;

namespace LogViewerWeb.Services.Repository
{
    /// <summary>
    /// 任务表仓储类
    /// </summary>
    public interface NginxAppLogRepository : JpaRepository<NginxAppLog, int>
    {
    }
}