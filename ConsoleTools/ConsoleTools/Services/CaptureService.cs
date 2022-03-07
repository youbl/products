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

        public string Operate(string saveFile)
        {
            string file;
            if (!string.IsNullOrWhiteSpace(saveFile))
            {
                file = saveFile;
            }
            else
            {
                file = Path.Combine(path, DateTime.Now.ToString("yyyyMMddHHmmss") + ".jpg");
            }

            CaptureImg.Default.CaptureScreenToFile(file, ImageFormat.Jpeg);
            return file;
        }
    }
}