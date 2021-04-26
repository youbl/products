using System.Collections.Generic;
using Beinet.Repository.Repositories;

namespace LogViewerWeb.Services.Repository
{
    /// <summary>
    /// 任务表仓储类
    /// </summary>
    public interface NginxAppLogRepository : JpaRepository<NginxAppLog, int>
    {
        [Query(
            "SELECT app,FLOOR(HOUR/100) hour,sum(num) num FROM #{#entityName} " +
            "WHERE app in (?1) AND hour BETWEEN ?2 AND ?3 AND isFront=?4 " +
            "GROUP BY app,FLOOR(HOUR/100) ORDER BY app,hour")]
        List<NginxAppLog> GroupByAppAndDay(List<string> app, int start, int end, int front);

        [Query(
            "SELECT FLOOR(HOUR/100) hour,sum(num) num FROM #{#entityName} " +
            "WHERE hour BETWEEN ?1 AND ?2 AND isFront=?3 " +
            "GROUP BY FLOOR(HOUR/100) ORDER BY hour")]
        List<NginxAppLog> GroupByAppAndDay(int start, int end, int front);


        [Query("SELECT app,hour,sum(num) num FROM #{#entityName} " +
               "WHERE app in (?1) AND hour BETWEEN ?2 AND ?3 AND isFront=?4 " +
               "GROUP BY app,hour ORDER BY app,hour")]
        List<NginxAppLog> GroupByAppAndHour(List<string> app, int start, int end, int front);

        [Query("SELECT hour,sum(num) num FROM #{#entityName} " +
               "WHERE hour BETWEEN ?1 AND ?2 AND isFront=?3 " +
               "GROUP BY hour ORDER BY hour")]
        List<NginxAppLog> GroupByAppAndHour(int start, int end, int front);
    }
}