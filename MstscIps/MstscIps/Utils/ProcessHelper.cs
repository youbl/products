using System;
using System.Diagnostics;
using System.Runtime.InteropServices;

namespace MstscIps.Utils
{
    internal static class ProcessHelper
    {
        [DllImport("User32.dll", EntryPoint = "FindWindow")]
        private static extern IntPtr FindWindow(string lpClassName, string lpWindowName);

        [DllImport("User32.dll", EntryPoint = "FindWindowEx")]
        private static extern IntPtr FindWindowEx(IntPtr hwndParent, IntPtr hwndChildAfter, string lpClassName,
            string lpWindowName);


        [DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)]
        private static extern bool SetWindowText(IntPtr hwnd, string lpString);

        public static void ModifyWindowTitle()
        {
            foreach (var process in Process.GetProcesses())
            {
                // 10.100.73.162 - 远程桌面连接    mstsc    13580
                // sb.AppendLine(process.MainWindowTitle + "    " + process.ProcessName + "    " + process.Id);
                var pname = process.ProcessName;
                if (string.IsNullOrEmpty(pname) || !pname.Equals("mstsc", StringComparison.OrdinalIgnoreCase))
                    continue;
                // var winInptr = FindWindow(null, process.MainWindowTitle);
                // 这个只能修改窗口标题，不能修改远程桌面的全屏标题
                var result = SetWindowText(process.MainWindowHandle, "哈哈看3");
            }
        }
    }
}