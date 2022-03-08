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
            var ret = SystemHelper.GetResolution();
            if (!string.IsNullOrWhiteSpace(saveFile))
            {
                FileHelper.WriteToFile(saveFile, ret);
            }

            return ret;
        }
    }
}