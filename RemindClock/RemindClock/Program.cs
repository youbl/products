using System;
using System.Diagnostics;
using System.Reflection;
using System.Threading;
using System.Windows.Forms;
using Beinet.Core.Cron;
using NLog;

namespace RemindClock
{
    static class Program
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main()
        {
            // 全局异常捕获
            AppDomain.CurrentDomain.UnhandledException += CurrentDomain_UnhandledException;
            DoStartLog();

            // 启动计划任务
            ScheduledWorker.StartAllScheduled();

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(MainForm.Default);
        }

        static void DoStartLog()
        {
            var ass = Assembly.GetExecutingAssembly();
            var process = Process.GetCurrentProcess();
            var procid = process.Id.ToString();

            ThreadPool.GetMinThreads(out var minworkthreads, out var miniocpthreads);
            var thid = Thread.CurrentThread.ManagedThreadId.ToString();
            string msg = string.Format("启动目录:{0}\r\n启动文件:{1}\r\n{2}\r\n.Net:{3}\r\n" +
                                       "当前进程/线程ID:{4}/{5}\r\n最小工作线程数/IO线程数:{6}/{7}",
                AppDomain.CurrentDomain.BaseDirectory,
                process.MainModule?.FileName,
                ass,
                ass.ImageRuntimeVersion,
                procid,
                thid,
                minworkthreads.ToString(),
                miniocpthreads.ToString());
            Console.WriteLine(msg);
            logger.Info(msg);
        }

        static void CurrentDomain_UnhandledException(object sender, UnhandledExceptionEventArgs e)
        {
            object exp = e == null ? "" : e.ExceptionObject;
            logger.Error("CurrentDomain_UnhandledExp:" + exp);
        }
    }
}