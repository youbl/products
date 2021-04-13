using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Reflection;
using System.Threading;
using NLog;

namespace LogAnalyse
{
    static class Program
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();

        static void Main()
        {
            AppDomain.CurrentDomain.UnhandledException += CurrentDomain_UnhandledException;
            DoStartLog();

            // 主调方法
            var methods = new JobOperator().GetIJobClass();
            RunMethod(methods);

            DoStopLog();
        }

        static void RunMethod(List<ThreadStart> methods)
        {
            if (methods == null || methods.Count == 0)
            {
                logger.Warn("未找到可启动的Job");
                return;
            }

            var threads = new Thread[methods.Count];
            int idx = 0;
            foreach (ThreadStart method in methods)
            {
                var thread = new Thread(method) {IsBackground = true};
                thread.Start();
                threads[idx] = thread;
                idx++;
            }

            logger.Info("启动任务个数：" + idx.ToString());

            // 堵塞等待所有任务完成
            foreach (var thread in threads)
            {
                thread.Join();
            }

            // 避免线程线束，还有语句未完毕
            Thread.Sleep(5000);

            logger.Info("任务全部线程结束");
        }

        static void CurrentDomain_UnhandledException(object sender, UnhandledExceptionEventArgs e)
        {
            object exp = e == null ? "" : e.ExceptionObject;
            logger.Error("CurrentDomain_UnhandledExp:" + exp);
        }


        /// <summary>
        /// 程序停止日志
        /// </summary>
        static void DoStopLog()
        {
            logger.Warn("程序退出");
        }


        /// <summary>
        /// 程序启动日志
        /// </summary>
        static void DoStartLog()
        {
            var ass = Assembly.GetExecutingAssembly();
            var process = Process.GetCurrentProcess();
            var procid = process.Id.ToString();
            ThreadPool.GetMinThreads(out var minworkthreads, out var miniocpthreads);
            var thid = Thread.CurrentThread.ManagedThreadId.ToString();
            string msg =
                $"启动目录:{AppDomain.CurrentDomain.BaseDirectory}\r\n" +
                $"启动文件:{process.MainModule?.FileName}\r\n" +
                $"{ass}\r\n.Net:{ass.ImageRuntimeVersion}\r\n" +
                $"当前进程/线程ID:{procid}/{thid}\r\n" +
                $"最小工作线程数/IO线程数:{minworkthreads.ToString()}/{miniocpthreads.ToString()}";
            Console.WriteLine(msg);
            logger.Warn(msg);
        }
    }
}