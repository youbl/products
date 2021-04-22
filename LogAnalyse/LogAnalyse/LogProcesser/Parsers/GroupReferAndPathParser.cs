using System;
using System.Collections.Generic;
using System.Configuration;
using System.Text;
using System.Text.RegularExpressions;
using LogAnalyse.LogProcesser.Repository;
using LogAnalyse.Utils;
using NLog;

namespace LogAnalyse.LogProcesser.Parsers
{
    /// <summary>
    /// 按referer+path进行分类汇总的解析器，
    /// 每天约60万行数据
    /// </summary>
    class GroupReferAndPathParser : IParser
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

        private readonly BaseSqlHelper sqlHelper =
            BaseSqlHelper.GetConnection<MySqlHelper>(ConfigurationManager.AppSettings["DB_DEFAULT"]);

        private readonly Regex pathRgx = new Regex(@"(?<=/)\d+(?=(/|$))", RegexOptions.Compiled); // 数字匹配
        private readonly Regex longRgx = new Regex(@"(?<=/)[^/]{32,}(?=(/|$))", RegexOptions.Compiled); // 32位以上字符匹配
        private readonly Regex httpRgx = new Regex(@"\s+HTTP/\d\.\d\s*", RegexOptions.Compiled); // 请求里的协议匹配

        private Dictionary<string, int> groups = new Dictionary<string, int>();

        public void Parse(NginxLog ngingLog)
        {
            try
            {
                var time = ngingLog.Time.ToString("yyyyMMddHH");
                var referer = GetUriPattern(ngingLog.Referer);
                if (!referer.StartsWith("http", StringComparison.OrdinalIgnoreCase))
                {
                    referer = "-"; // referer不应该没有http的
                }

                var url = GetUriPattern(ngingLog.Request);
                var identify = time + '\n' + referer + '\n' + url;
                lock (groups)
                {
                    groups.TryGetValue(identify, out var num);
                    groups[identify] = num + 1;
                }
            }
            catch (Exception exp)
            {
                logger.Error(ngingLog.Filename + " " + exp);
            }
        }

        public void Finish(string day)
        {
            Dictionary<string, int> groupsInner;
            lock (groups)
            {
                if (groups.Count <= 0)
                    return;
                groupsInner = groups;
                groups = new Dictionary<string, int>();
            }
            var sql = new StringBuilder(groupsInner.Count * 200);
            var num = 0;
            var totalNum = 0;
            foreach (var row in groupsInner)
            {
                if (sql.Length > 0)
                    sql.Append(",");
                sql.Append("(");
                var arr = row.Key.Split('\n');
                sql.AppendFormat("{0},'{1}','{2}',{3}",
                    arr[0],
                    StrHelper.ProcessSqlVal(arr[1]),
                    StrHelper.ProcessSqlVal(arr[2]),
                    row.Value);
                sql.Append(")");
                num++;
                if (num % 10000 == 0)
                {
                    var perNum = DoInsert(sql.ToString());
                    logger.Info("汇总已插入行数：" + perNum);
                    totalNum += perNum;
                    sql.Length = 0;
                }
            }

            totalNum += DoInsert(sql.ToString());
            logger.Info("汇总共插入行数：" + totalNum);
        }

        private int DoInsert(string sql)
        {
            sql = "INSERT INTO nginxtotallog(`date`,`referer`,`request`,`num`)VALUES" + sql;
            return sqlHelper.ExecuteNonQuery(sql);
        }

        private string GetUriPattern(string uri)
        {
            if (string.IsNullOrEmpty(uri))
            {
                return "-";
            }

            var idx = uri.IndexOf('?');
            if (idx > 0)
            {
                uri = uri.Substring(0, idx);
            }

            uri = pathRgx.Replace(uri, "*");
            uri = longRgx.Replace(uri, "*");
            uri = httpRgx.Replace(uri, "");
            uri = uri.Trim();
            if (uri.Length == 0)
                return "-";
            return uri;
        }
    }
}