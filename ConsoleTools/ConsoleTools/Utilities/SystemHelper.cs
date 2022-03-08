using System;
using System.Management;
using System.Text;

namespace ConsoleTools.Utilities
{
    internal static class SystemHelper
    {
        public static string GetPhysicalMemort()
        {
            var mos = "SELECT TotalPhysicalMemory FROM Win32_ComputerSystem";
            var result = new StringBuilder();
            ManagementObjectQuery(mos, record =>
            {
                if (result.Length > 0)
                {
                    result.AppendLine();
                }

                result.AppendFormat("{0}", ByteToStr(record["TotalPhysicalMemory"]));
            });

            return result.ToString();
        }

        public static string GetResolution()
        {
            var mos = "SELECT CurrentHorizontalResolution,CurrentVerticalResolution FROM Win32_VideoController";
            var result = new StringBuilder();
            ManagementObjectQuery(mos, record =>
            {
                if (result.Length > 0)
                {
                    result.AppendLine();
                }

                result.AppendFormat("{0}*{1}",
                    record["CurrentHorizontalResolution"],
                    record["CurrentVerticalResolution"]);
            });

            return result.ToString();
        }

        private static void ManagementObjectQuery(string mosSql, Action<ManagementBaseObject> action)
        {
            //var sb = new StringBuilder();
            using (var mydisplayResolution = new ManagementObjectSearcher(mosSql))
            {
                foreach (var record in mydisplayResolution.Get())
                {
                    // foreach (var property in record.Properties)
                    // {
                    //     sb.AppendFormat("{0} : {1}\n", property.Name, property.Value);
                    // }
                    // foreach (var property in record.SystemProperties)
                    // {
                    //     sb.AppendFormat("{0} : {1}\n", property.Name, property.Value);
                    // }
                    action(record);
                }
            }
        }

        private static string ByteToStr(object byteSize)
        {
            if (byteSize == null)
                return "0";
            long size = long.Parse(byteSize.ToString());
            return ByteToStr(size);
        }

        private static string ByteToStr(long byteSize)
        {
            if (byteSize < 1024)
                return byteSize.ToString() + "字节";
            byteSize = (long) Math.Ceiling(byteSize / 1024.0);
            if (byteSize < 1024)
                return byteSize.ToString() + "KB";
            byteSize = (long) Math.Ceiling(byteSize / 1024.0);
            if (byteSize < 1024)
                return byteSize.ToString() + "MB";
            byteSize = (long) Math.Ceiling(byteSize / 1024.0);
            if (byteSize < 1024)
                return byteSize.ToString() + "GB";

            byteSize = (long) Math.Ceiling(byteSize / 1024.0);
            return byteSize.ToString() + "TB";
        }
    }
}