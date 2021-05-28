using System;
using System.Windows.Forms;
using Beinet.Core.Cron;

namespace RemindClock
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main()
        {
            // 启动计划任务
            ScheduledWorker.StartAllScheduled();

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainForm());
        }
    }
}
