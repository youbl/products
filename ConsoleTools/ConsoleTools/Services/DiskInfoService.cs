using System;
using System.Text;
using System.Text.RegularExpressions;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    internal class DiskInfoService : ServicesBase
    {
        public string Operate(string[] args)
        {
            var ret = SystemHelper.GetDiskInfo();

            var saveFile = args.Length > 0 ? args[0] : "";
            if (!string.IsNullOrWhiteSpace(saveFile))
            {
                FileHelper.WriteToFile(saveFile, ret);
            }

            return ret;
        }
    }
}