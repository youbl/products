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

        public void Finish()
        {
            if (groups.Count <= 0)
                return;
            var sql = new StringBuilder(groups.Count * 200);
            var num = 0;
            var totalNum = 0;
            foreach (var row in groups)
            {
                if (sql.Length > 0)
                    sql.Append(",");
                sql.Append("(");
                var arr = row.Key.Split('\n');
                var app = arr[2].Replace("'", "");
                if (app.Length > 200)
                {
                    logger.Warn(app);
                    app = app.Substring(0, 200);
                }

                sql.AppendFormat("{0},'{1}','{2}',{3}",
                    arr[0],
                    arr[1].Replace("'", ""),
                    app,
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
            //Console.WriteLine("==="+sql+"===");
            sql = "INSERT INTO nginxapplog(`hour`,`isfront`,`app`,`num`)VALUES" + sql;
            return sqlHelper.ExecuteNonQuery(sql);
        }

        private string GetUriApp(string uri)
        {
            if (uri == null)
            {
                return "-";
            }

            uri = uri.Trim();
            if (uri.Length == 0)
            {
                return "-";
            }

            var idx = uri.IndexOf(' ');
            if (idx > 0)
            {
                uri = uri.Substring(idx + 1);
            }

            if (uri[0] == '/')
            {
                uri = uri.Substring(1);
            }

            idx = uri.IndexOf('/');
            if (idx > 0)
            {
                uri = uri.Substring(0, idx);
            }

            idx = uri.IndexOf(' ');
            if (idx > 0)
            {
                uri = uri.Substring(0, idx);
            }

            uri = uri.Trim();
            if (uri.Length == 0)
                return "-";
            return uri;
        }
    }
}