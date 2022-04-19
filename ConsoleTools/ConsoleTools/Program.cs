using System;
using System.Collections.Generic;

namespace ConsoleTools
{
    class Program
    {
        /// <summary>
        /// 参数1
        /// </summary>
        /// <param name="args"></param>
        static void Main(string[] args)
        {
            AppDomain.CurrentDomain.UnhandledException += CurrentDomain_UnhandledException;

            // var filename = Path.Combine(AppDomain.CurrentDomain.BaseDirectory,
            //     DateTime.Now.ToString("yyyyMMddHHmmss") + ".jpg");
            // using (Image img = CaptureImg3.GetWindow("钉钉"))
            // {
            //     img.Save(filename, ImageFormat.Jpeg);
            // }
            //
            // return;


            if (args == null || args.Length <= 0)
            {
                LoopChoose();
                return;
            }

            try
            {
                Console.WriteLine(MainService.CallService(args[0], GetLeftArr(args)));
            }
            catch (Exception exp)
            {
                Console.WriteLine("CallService出错:" + exp);
            }
        }

        static void LoopChoose()
        {
            var line = "";
            Console.WriteLine("请输入参数1： " +
                              "\n  无参数时，显示本菜单，有参数时，执行完自动退出 " +
                              "\n  1 全屏截图，参数2为截图文件路径(可空或传-)，" +
                              "\n              参数3为截图格式(默认jpg，支持jpg/png/bmp)，" +
                              "\n              参数4为main表示截主屏，默认多屏 " +
                              "\n  2 获取开机时间，参数2为写入文件路径(可空) " +
                              "\n  3 获取当前分辨率，参数2为写入文件路径(可空) " +
                              "\n  4 获取当前支持的分辨率，参数2为写入文件路径(可空) " +
                              "\n  5 设置分辨率，参数2为分辨率，如1920*1080 " +
                              "\n  6 获取系统配置，参数2为写入文件路径(可空)" +
                              "\n  7 获取机器码，参数2为写入文件路径(可空)" +
                              "\n  8 获取磁盘信息，参数2为写入文件路径(可空)");
            while (!line.Equals("exit", StringComparison.OrdinalIgnoreCase) &&
                   !line.Equals("quit", StringComparison.OrdinalIgnoreCase) &&
                   !line.Equals("q", StringComparison.OrdinalIgnoreCase))
            {
                Console.WriteLine();
                line = (Console.ReadLine() ?? "").Trim();
                if (line.Length > 0)
                {
                    var arrArgs = line.Split(' ');
                    Console.WriteLine(MainService.CallService(arrArgs[0], GetLeftArr(arrArgs)));
                }
            }
        }

        static string[] GetLeftArr(string[] args)
        {
            var ret = new List<string>(args.Length - 1);
            for (int i = 1, j = args.Length; i < j; i++)
            {
                ret.Add(args[i]);
            }

            return ret.ToArray();
        }

        static void CurrentDomain_UnhandledException(object sender, UnhandledExceptionEventArgs e)
        {
            // object exp = e == null ? "" : e.ExceptionObject;
            // MessageBox.Show("CurrentDomain_UnhandledExp:" + exp);
        }
    }
}