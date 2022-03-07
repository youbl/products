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
            //DoScan(@"d:\tmp\aaa");
            if (args == null || args.Length <= 0)
            {
                Console.WriteLine("请输入参数： 1截图 2开机时间 3分辨率，第2个参数为输出文件路径(可空)");
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

            // Console.WriteLine(result);
            // if (args.Length >= 2)
            // {
            //     try
            //     {
            //         FileHelper.WriteToFile(args[1], result);
            //     }
            //     catch (Exception exp)
            //     {
            //         Console.WriteLine("写文件错:" + exp);
            //     }
            // }
        }
    }
}