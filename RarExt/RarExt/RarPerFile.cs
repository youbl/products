using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace RarExt
{
    /// <summary>
    /// 遍历指定目录，对只有文件，没有子目录的目录进行压缩
    /// </summary>
    class RarPerFile
    {
        private static string RarPath =
            ConfigurationManager.AppSettings["rarPath"] ?? @"c:\Program Files\WinRAR\Rar.exe";

        static Dictionary<string, int> zipNames = new Dictionary<string, int>();

        public void Run(string dir)
        {
            using (var writer = new StreamWriter(@"D:\a.bat", false, Encoding.GetEncoding("UTF-8")))// GB2312遇到非中文，会乱码出错
            {
                DoScan(dir, writer);
            }
        }

        void DoScan(string dir, StreamWriter writer)
        {
            if (!Directory.Exists(dir))
            {
                Console.WriteLine("指定的目录不存在: " + dir);
                return;
            }

            foreach (var subDir in Directory.GetDirectories(dir))
            {
                DoScan(subDir, writer);
            }

            foreach (var file in Directory.GetFiles(dir))
            {
                DoRar(file, writer);
            }
        }

        static void DoRar(string file, StreamWriter writer)
        {
            var ext = Path.GetExtension(file);
            if (ext == null || ext.ToLower() != ".mp3")
            {
                using (var writer2 = new StreamWriter(@"D:\a.txt", true, Encoding.GetEncoding("GB2312")))
                {
                    writer2.WriteLine(file);
                }
            }

            if (ext != null && ext.ToLower() == ".rar") return;


            var zipFilePath = Path.Combine(Path.GetDirectoryName(file) ?? "",
            Path.GetFileNameWithoutExtension(file) + ".rar");

            // a表示创建压缩文件 -p表示设置密码 -r表示递归子目录 -ep1表示只保留最后一级目录
            var command = $"\"{RarPath}\" a -ptxt.main -ep1 \"{zipFilePath}\" \"{file}\"";
            writer.WriteLine(command);

        }
    }
}