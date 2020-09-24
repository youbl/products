
using System;

namespace RarExt
{
    class Program
    {
        static void Main(string[] args)
        {
            //DoScan(@"d:\tmp\aaa");
            if (args == null || args.Length <= 0)
            {
                Console.WriteLine("请输入要压缩的目录");
                return;
            }

            new RarLastDirNoSubDir().DoScan(args[0]);
        }
    }
}
