using System;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    /// <summary>
    /// 获取机器名和登录账号的服务
    /// </summary>
    internal class ServerNameService : ServicesBase
    {
        public string Operate(string[] args)
        {
            var saveFile = args.Length > 0 ? args[0] : "";
            var ret = string.Format("机器名:{0} 用户:{1} ({2} {3}位 CPU:{4}核 内存:{5})",
                Environment.MachineName,
                Environment.UserName,
                Environment.OSVersion,
                Environment.Is64BitOperatingSystem ? "64" : "32",
                Environment.ProcessorCount,
                SystemHelper.GetPhysicalMemort());

            if (!string.IsNullOrWhiteSpace(saveFile))
            {
                FileHelper.WriteToFile(saveFile, ret);
            }

            return ret;
        }
    }
}