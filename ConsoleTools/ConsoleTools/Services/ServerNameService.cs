using System;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    /// <summary>
    /// 获取机器名和登录账号的服务
    /// </summary>
    internal class ServerNameService : ServicesBase
    {
        public string Operate(string saveFile)
        {
            var ret = string.Format("{0}\\{1} ({2} {3}位 CPU:{4}核 内存:{5})",
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