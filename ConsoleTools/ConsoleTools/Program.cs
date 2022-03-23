using System;

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
            if (args == null || args.Length <= 0)
            {
                LoopChoose();
                return;
            }

            var saveFile = "";
            if (args.Length >= 2)
            {
                saveFile = args[1];
            }

            try
            {
                Console.WriteLine(MainService.CallService(args[0], saveFile));
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
                              "\n  1 全屏截图，参数2为截图文件路径(可空) " +
                              "\n  2 获取开机时间，参数2为写入文件路径(可空) " +
                              "\n  3 获取当前分辨率，参数2为写入文件路径(可空) " +
                              "\n  4 获取当前支持的分辨率，参数2为写入文件路径(可空) " +
                              "\n  5 设置分辨率，参数2为分辨率，如1920*1080 " +
                              "\n  6 获取系统配置，参数2为写入文件路径(可空)");
            while (!line.Equals("exit", StringComparison.OrdinalIgnoreCase) &&
                   !line.Equals("quit", StringComparison.OrdinalIgnoreCase) &&
                   !line.Equals("q", StringComparison.OrdinalIgnoreCase))
            {
                Console.WriteLine();
                line = (Console.ReadLine() ?? "").Trim();
                if (line.Length > 0)
                {
                    var arrArgs = line.Split(' ');
                    Console.WriteLine(MainService.CallService(arrArgs[0], arrArgs.Length > 1 ? arrArgs[1] : ""));
                }

            }
        }
    }
}