using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Configuration;
using System.Globalization;
using System.Text;
using System.Text.RegularExpressions;
using LogAnalyse.LogProcesser.Repository;
using LogAnalyse.Utils;
using NLog;

namespace LogAnalyse.LogProcesser.Parsers
{
    /// <summary>
    /// 分类汇总的解析器
    /// </summary>
    class GroupParser : IParser
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

        private BaseSqlHelper sqlHelper =
            BaseSqlHelper.GetConnection<MySqlHelper>(ConfigurationManager.AppSettings["DB_DEFAULT"]);

        private Regex pathRgx = new Regex(@"(?<=/)\d+(?=(/|$))", RegexOptions.Compiled); // 数字匹配
        private Regex longRgx = new Regex(@"(?<=/)[^/]{32,}(?=(/|$))", RegexOptions.Compiled); // 32位以上字符匹配
        private Regex httpRgx = new Regex(@"\s+HTTP/\d\.\d\s*", RegexOptions.Compiled); // 请求里的协议匹配

        // 这2个属性用于解析nginx日志里的时间
        private CultureInfo cultureInfo = new System.Globalization.CultureInfo("en-us");
        private string nginxTimeFormat = "dd/MMM/yyyy:HH:mm:ss +0800";

        private Dictionary<string, int> groups = new Dictionary<string, int>();

        public void Parse(NginxLog ngingLog)
        {
            try
            {
                var time = ParseDate(ngingLog.Timelocal);
                var referer = GetUriPattern(ngingLog.Referer);
                var url = GetUriPattern(ngingLog.Request);
                var identify = time.ToString() + '\n' + referer + '\n' + url;
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

        private int ParseDate(string timeLocal)
        {
            try
            {
                var dt = DateTime.ParseExact(timeLocal, nginxTimeFormat, cultureInfo);
                dt = dt.AddHours(8); // 要加8
                return int.Parse(dt.ToString("yyyyMMddHH"));
            }
            catch (Exception exp)
            {
                logger.Error("{0} error:{1}", timeLocal, exp.Message);
                return 0;
            }
        }

        public void Finish()
        {
            var sql = new StringBuilder(groups.Count * 200);
            var num = 0;
            var totalNum = 0;
            foreach (var row in groups)
            {
                if (sql.Length > 0)
                    sql.Append(",");
                sql.Append("(");
                var arr = row.Key.Split('\n');
                sql.AppendFormat("{0},'{1}','{2}',{3}",
                    arr[0],
                    arr[1].Replace("'", ""),
                    arr[2].Replace("'", ""),
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
            return sqlHelper.ExecuteNonQuery(sql.ToString());
        }

        private string GetUriPattern(string uri)
        {
            if (string.IsNullOrEmpty(uri))
            {
                return "";
            }

            var idx = uri.IndexOf('?');
            if (idx > 0)
            {
                uri = uri.Substring(0, idx);
            }

            uri = pathRgx.Replace(uri, "*");
            uri = longRgx.Replace(uri, "*");
            uri = httpRgx.Replace(uri, "");
            return uri.Trim();
        }
    }
}