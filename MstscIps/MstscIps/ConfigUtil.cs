using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using MstscIps.Feign.Dto;
using MstscIps.Utils;

namespace MstscIps
{
    internal class ConfigUtil
    {
        public static ConfigUtil Default = new ConfigUtil();

        private ConfigUtil()
        {
        }

        private readonly string configFile = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "url.txt");

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
                    var configItem = SplitConfig(row);
                    if (configItem == null)
                    {
                        return ("第" + rowIdx + "行，不符合格式：说明,URL,VPS密码");
                    }

                    saveSb.Append(configItem.Desc)
                        .Append(',')
                        .Append(configItem.Url)
                        .Append(',')
                        .Append(configItem.Pwd)
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

        public ConfigItem SplitConfig(string item)
        {
            var row = item.Trim();
            if (row.Length == 0)
            {
                return null;
            }

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

            var type = GetProtocol(url);
            if (type == ConfigType.None)
            {
                return null;
            }

            return ConfigItem.Create(desc, url, pwd, type);
        }

        /// <summary>
        /// 是否允许的协议，比如http 或 本地文件
        /// </summary>
        /// <param name="url"></param>
        /// <returns></returns>
        private ConfigType GetProtocol(string url)
        {
            if (Regex.IsMatch(url, @"^https?://[^/]+", RegexOptions.IgnoreCase))
            {
                return ConfigType.Url;
            }

            if (Regex.IsMatch(url, @"^[c-z]:\\[^\\]+", RegexOptions.IgnoreCase))
            {
                return ConfigType.File;
            }

            return ConfigType.None;
        }

        public List<VpsMachineDto> ReadIpFile(string file)
        {
            try
            {
                if (!File.Exists(file))
                {
                    return null;
                }

                var ret = new List<VpsMachineDto>();
                using (var sr = new StreamReader(file, Encoding.UTF8))
                {
                    while (!sr.EndOfStream)
                    {
                        var line = (sr.ReadLine() ?? "").Trim();
                        if (line.Length == 0)
                        {
                            continue;
                        }

                        var dto = ParseToDto(line);
                        if (dto != null)
                        {
                            //dto.GroupCode = file;
                            ret.Add(dto);
                        }
                    }
                }

                return ret;
            }
            catch (Exception exp)
            {
                MessageBox.Show(file + ": " + exp.Message);
                return null;
            }
        }

        private VpsMachineDto ParseToDto(string line)
        {
            var idx = line.IndexOf(':');
            if (idx <= 0 || idx >= line.Length - 1)
                return null;
            var desc = line.Substring(0, idx);

            line = line.Substring(idx + 1);
            idx = line.IndexOf(':');
            var ip = line.Substring(0, idx);
            if (!StrHelper.IsIp(ip))
                return null;

            line = line.Substring(idx + 1);
            idx = line.IndexOf(':');
            var username = line.Substring(0, idx);
            var pwd = line.Substring(idx + 1);

            var ret = new VpsMachineDto();
            ret.VpsIp = ip;
            ret.User = username;
            ret.VpsPwd = pwd;
            ret.GroupCode = desc;
            return ret;
        }
    }

    public class ConfigItem
    {
        public static ConfigItem Create(string desc, string url, string pwd, ConfigType type)
        {
            var ret = new ConfigItem();
            ret.Url = url;
            ret.Desc = desc;
            ret.Pwd = pwd;
            ret.Type = type;
            return ret;
        }

        public string Desc { get; set; }
        public string Url { get; set; }
        public string Pwd { get; set; }
        public ConfigType Type { get; set; }
    }

    public enum ConfigType
    {
        /// <summary>
        /// 未知协议
        /// </summary>
        None,

        /// <summary>
        /// http协议的url
        /// </summary>
        Url,

        /// <summary>
        /// 本地文件
        /// </summary>
        File
    }
}