using System;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    /// <summary>
    /// 获取系统开机时间的服务
    /// </summary>
    internal class RuntimeService : ServicesBase
    {
        public string Operate(string[] args)
        {
            var saveFile = args.Length > 0 ? args[0] : "";
            var runMiis = Environment.TickCount;
            var ret = DateTime.Now.AddMilliseconds(-runMiis).ToString("yyyy-MM-dd HH:mm:ss.fff");

            if (!string.IsNullOrWhiteSpace(saveFile))
            {
                FileHelper.WriteToFile(saveFile, ret);
            }

            return ret;
        }
    }
}