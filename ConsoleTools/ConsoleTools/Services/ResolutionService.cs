using System;
using System.Management;
using System.Text;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    /// <summary>
    /// 获取系统分辨率的服务
    /// </summary>
    internal class ResolutionService : ServicesBase
    {
        public string Operate(string saveFile)
        {
            var result = new StringBuilder();
            using (var mydisplayResolution =
                   new ManagementObjectSearcher(
                       "SELECT CurrentHorizontalResolution, CurrentVerticalResolution FROM Win32_VideoController"))
            {
                foreach (var record in mydisplayResolution.Get())
                {
                    if (result.Length > 0)
                    {
                        result.AppendLine();
                    }

                    result.AppendFormat("{0}*{1}", record["CurrentHorizontalResolution"],
                        record["CurrentVerticalResolution"]);
                    // Console.WriteLine("横向像素: " + record["CurrentHorizontalResolution"]);
                    // Console.WriteLine("纵向像素: " + record["CurrentVerticalResolution"]);
                }
            }

            var ret = result.ToString();
            if (!string.IsNullOrWhiteSpace(saveFile))
            {
                FileHelper.WriteToFile(saveFile, ret);
            }

            return ret;
        }
    }
}