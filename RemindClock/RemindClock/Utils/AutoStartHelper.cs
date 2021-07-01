using System;
using System.IO;
using System.Windows.Forms;
using Microsoft.Win32;

namespace RemindClock.Utils
{
    /// <summary>
    /// 开机启动操作辅助类
    /// </summary>
    static class AutoStartHelper
    {
        private const string RUN_PATH = @"SOFTWARE\Microsoft\Windows\CurrentVersion\Run";

        /// <summary>  
        /// 修改程序在注册表中的键值实现启动与否
        /// </summary>  
        /// <param name="regName">开机启动的注册表键名</param> 
        /// <param name="isAuto">true:开机启动,false:不开机自启</param> 
        /// <param name="exePath">开机启动的exe路径，为空表示当前exe</param> 
        public static void AutoStart(string regName = null, bool isAuto = true, string exePath = null)
        {
            exePath = exePath ?? Application.ExecutablePath;
            regName = regName ?? Path.GetFileName(exePath);

            using (var regUser = Registry.CurrentUser)
            using (var regRun = regUser.CreateSubKey(RUN_PATH))
            {
                if (regRun == null)
                    throw new Exception(RUN_PATH + " 读取到NULL");

                // 设置开机自启动
                if (isAuto)
                    regRun.SetValue(regName, exePath);
                else
                    regRun.DeleteValue(regName, false);
            }
        }

        /// <summary>
        /// 指定的程序是否已设置自动启动
        /// </summary>
        /// <param name="regName"></param>
        /// <param name="exePath"></param>
        /// <returns></returns>
        public static bool IsAutoStart(string regName = null, string exePath = null)
        {
            exePath = exePath ?? Application.ExecutablePath;
            regName = regName ?? Path.GetFileName(exePath);

            string existPath;
            using (var regUser = Registry.CurrentUser)
            using (var regRun = regUser.CreateSubKey(RUN_PATH))
            {
                if (regRun == null)
                    throw new Exception(RUN_PATH + " 读取到NULL");

                var regValue = regRun.GetValue(regName);
                if (regValue == null)
                    return false;
                existPath = regValue.ToString();
            }

            // 路径相同，认为true
            return (existPath == exePath);
        }
    }
}