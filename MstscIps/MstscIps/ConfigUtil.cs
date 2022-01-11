using System;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace MstscIps
{
    internal class ConfigUtil
    {
        public static ConfigUtil Default = new ConfigUtil();

        private ConfigUtil()
        {
        }

        private string configFile = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "url.txt");

        public string[] ReadConfigList()
        {
            var str = ReadConfig();
            return str.Split(new[] {'\r', '\n'}, StringSplitOptions.RemoveEmptyEntries);
        }

        public string ReadConfig()
        {
            try
            {
                if (!File.Exists(configFile))
                {
                    return "";
                }

                using (var sr = new StreamReader(configFile, Encoding.UTF8))
                {
                    return sr.ReadToEnd();
                }
            }
            catch (Exception exp)
            {
                return exp.ToString();
            }
        }

        public string SaveConfig(string str)
        {
            try
            {
                var saveSb = new StringBuilder();

                var config = str.Split('\r', '\n');
                int rowIdx = 0;
                foreach (var strRow in config)
                {
                    rowIdx++;
                    var row = strRow.Trim();
                    if (row.Length == 0)
                        continue;
                    var arr = SplitConfig(row);
                    if (arr == null || arr.Length != 3)
                    {
                        return ("第" + rowIdx + "行，不符合格式：说明,URL,VPS密码");
                    }

                    saveSb.Append(arr[0])
                        .Append(',')
                        .Append(arr[1])
                        .Append(',')
                        .Append(arr[2])
                        .AppendLine();
                }

                using (var sw = new StreamWriter(configFile, false, Encoding.UTF8))
                {
                    sw.Write(saveSb.ToString());
                }

                return "";
            }
            catch (Exception exp)
            {
                return exp.ToString();
            }
        }

        public string GetUrl(string item)
        {
            var arr = SplitConfig(item);
            if (arr == null || arr.Length != 3)
            {
                return null;
            }

            return arr[1];
        }

        public string[] SplitConfig(string item)
        {
            var row = item.Trim();
            if (row.Length == 0)
                return null;
            var idx = row.IndexOf(',');
            if (idx <= 0 || idx >= row.Length - 1)
            {
                return null;
            }

            var desc = row.Substring(0, idx).Trim();
            if (desc.Length == 0)
            {
                return null;
            }

            var urlAndPwd = row.Substring(idx + 1).Trim();
            idx = urlAndPwd.IndexOf(',');
            if (idx <= 0 || idx >= urlAndPwd.Length - 1)
            {
                return null;
            }

            var url = urlAndPwd.Substring(0, idx).Trim();
            var pwd = urlAndPwd.Substring(idx + 1).Trim();
            if (url.Length == 0 || pwd.Length == 0)
            {
                return null;
            }

            if (!Regex.IsMatch(url, @"^https?://[^/+]"))
            {
                return null;
            }

            return new[] {desc, url, pwd};
        }
    }
}