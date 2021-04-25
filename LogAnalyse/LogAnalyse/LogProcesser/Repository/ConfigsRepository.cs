using System.Collections.Generic;
using Beinet.Repository.Repositories;

namespace LogAnalyse.LogProcesser.Repository
{
    /// <summary>
    /// 配置表仓储类
    /// </summary>
    public interface ConfigsRepository : JpaRepository<Configs, int>
    {
        [Query("SELECT val FROM #{#entityName} where type=?1")]
        List<string> findAllVal(string type);
    }
}