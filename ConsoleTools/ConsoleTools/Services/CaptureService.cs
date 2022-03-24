using System;
using System.Drawing.Imaging;
using System.IO;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    /// <summary>
    /// Windows截图服务
    /// </summary>
    internal class CaptureService : ServicesBase
    {
        private static string path = AppDomain.CurrentDomain.BaseDirectory;

        public string Operate(string[] args)
        {
            string file;
            if (args.Length > 0 && args[0] != "-")
            {
                file = args[0];
            }
            else
            {
                file = Path.Combine(path, DateTime.Now.ToString("yyyyMMddHHmmss") + ".jpg");
            }

            var format = args.Length > 1 ? args[1] : "jpeg";

            CaptureImg2.Default.CaptureScreenToFile(file, GetFormat(format));
            return file;
        }

        ImageFormat GetFormat(string format)
        {
            format = (format ?? "jpeg").ToLower();
            switch (format)
            {
                default:
                    return ImageFormat.Jpeg;
                case "png":
                    return ImageFormat.Png;
                case "bmp":
                    return ImageFormat.Bmp;
            }
        }
    }
}