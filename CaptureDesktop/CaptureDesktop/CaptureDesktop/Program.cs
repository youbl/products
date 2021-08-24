using System;
using System.Drawing.Imaging;

namespace CaptureDesktop
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine(DateTime.Now + " 开始监听...");
            HttpHandler.Default.StartListen();

            // CaptureUtil.Default.CaptureScreenToFile("D:\\a.jpg", ImageFormat.Jpeg);
            Console.WriteLine(DateTime.Now + " 程序退出.");
        }
    }
}