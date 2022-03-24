using System;
using System.Management;
using System.Text;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    /// <summary>
    /// 获取系统所支持的分辨率
    /// </summary>
    internal class SupportResolutionService : ServicesBase
    {
        public string Operate(string[] args)
        {
            var saveFile = args.Length > 0 ? args[0] : "";

            var sb = new StringBuilder();
            sb.AppendLine("当前分辨率:" + DisplayHelper.GetCurrentSettings());
            var allModes = DisplayHelper.EnumerateSupportedModes();
            foreach (var item in allModes)
            {
                sb.AppendLine("  " + item);
            }

            var ret = sb.ToString();
            if (!string.IsNullOrWhiteSpace(saveFile))
            {
                FileHelper.WriteToFile(saveFile, ret);
            }

            return ret;
        }
    }
}