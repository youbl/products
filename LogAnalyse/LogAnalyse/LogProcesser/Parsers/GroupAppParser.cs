using System;
using System.Collections.Generic;
using System.Configuration;
using System.Text;
using LogAnalyse.LogProcesser.Repository;
using LogAnalyse.Utils;
using NLog;

namespace LogAnalyse.LogProcesser.Parsers
{
    /// <summary>
    /// 按app进行分类汇总的解析器，
    /// 每天约 行数据
    /// </summary>
    class GroupAppParser : IParser
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

        private readonly BaseSqlHelper sqlHelper =
            BaseSqlHelper.GetConnection<MySqlHelper>(ConfigurationManager.AppSettings["DB_DEFAULT"]);

        private Dictionary<string, int> groups = new Dictionary<string, int>();

        public void Parse(NginxLog ngingLog)
        {
            try
            {
                var time = ngingLog.Time.ToString("yyyyMMddHH");
                // 有refer且是http开头
                var isFront = (ngingLog.Referer != null &&
                               ngingLog.Referer.StartsWith("http", StringComparison.OrdinalIgnoreCase))
                    ? 1
                    : 0;

                var app = GetUriApp(ngingLog.Request);
                var identify = time + '\n' + isFront + '\n' + app;
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
                // 每小时访问量太小，不入库
                if (row.Value < 5)
                    continue;

                if (sql.Length > 0)
                    sql.Append(",");
                sql.Append("(");
                var arr = row.Key.Split('\n');
                var app = StrHelper.ProcessSqlVal(arr[2]);
                if (app.Length > 200)
                {
                    logger.Warn(app);
                    app = app.Substring(0, 200);
                }

                sql.AppendFormat("{0},'{1}','{2}',{3},{4}",
                    arr[0],
                    StrHelper.ProcessSqlVal(arr[1]),
                    app,
                    row.Value,
                    day); // 因为nginx日志有2天重叠，避免问题
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
            try
            {
                sql = "INSERT INTO nginxapplog(`hour`,`isfront`,`app`,`num`, `day`)VALUES" + sql;
                return sqlHelper.ExecuteNonQuery(sql);
            }
            catch (Exception exp)
            {
                logger.Error(exp.Message + "\r\n" + sql);
                return 0;
            }
        }

        private string GetUriApp(string originUri)
        {
            if (originUri == null)
            {
                return "-";
            }

            var uri = originUri.Trim();
            if (uri.Length == 0)
            {
                return "-";
            }

            var arr = uri.Split(' ');
            if (arr.Length <= 1)
            {
                return "-";
            }

            uri = arr[1]; // 去除 "GET /cc/test/ip.aspx HTTP1/1" 前面的GET和后面的HTTP
            var idx = uri.IndexOf('?');
            if (idx == 0)
            {
                return "-";
            }

            // 移除问号
            if (idx > 0)
            {
                uri = uri.Substring(0, idx);
            }

            uri = uri.Trim('/');

            idx = uri.IndexOf('/');
            if (idx > 0)
            {
                uri = uri.Substring(0, idx);
            }

            uri = uri.Trim();
            // 还有这种请求 \x04\x01\x00Pg)\xA7\xEA\x00
            if (uri.Length == 0 || uri.IndexOf('\\') >= 0 || uri.IndexOf('%') >= 0)
                return "-";
            if (IsStatic(uri))
            {
                return "front";
            }

            return uri;
        }

        static bool IsStatic(string uri)
        {
            uri = uri.ToLower();
            return uri.EndsWith(".js") ||
                   uri.EndsWith(".ico") ||
                   uri.EndsWith(".txt") ||
                   uri.EndsWith(".png") ||
                   uri.EndsWith(".css") ||
                   uri.EndsWith(".xml") ||
                   uri.EndsWith(".json") ||
                   uri.EndsWith(".html") ||
                   uri.EndsWith(".htm");
        }
    }
}