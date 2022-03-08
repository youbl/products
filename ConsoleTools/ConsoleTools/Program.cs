using System;
using ConsoleTools.Utilities;

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
            while (!line.Equals("exit", StringComparison.OrdinalIgnoreCase))
            {
                Console.WriteLine("请输入参数： 1截图 2开机时间 3当前分辨率 4支持的分辨率 5设置分辨率 6系统配置，第2个参数为输出文件路径(可空)");
                line = (Console.ReadLine() ?? "").Trim();
                if (line.Length > 0)
                {
                    var arrArgs = line.Split(' ');
                    Console.WriteLine(MainService.CallService(arrArgs[0], arrArgs.Length > 1 ? arrArgs[1] : ""));
                }

                Console.WriteLine();
            }
        }
    }
}