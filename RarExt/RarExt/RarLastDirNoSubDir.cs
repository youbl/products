
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace RarExt
{
    /// <summary>
    /// 遍历指定目录，对只有文件，没有子目录的目录进行压缩
    /// </summary>
    class RarLastDirNoSubDir
    {
        private static string RarPath =
            ConfigurationManager.AppSettings["rarPath"] ?? @"c:\Program Files\WinRAR\Rar.exe";

        static Dictionary<string, int> zipNames = new Dictionary<string, int>();
        
        public void DoScan(string dir)
        {
            if (!Directory.Exists(dir))
            {
                Console.WriteLine("指定的目录不存在: " + dir);
                return;
            }

            var subDirs = Directory.GetDirectories(dir);
            if (subDirs.Length <= 0)
            {
                DoRar(dir);
                return;
            }

            foreach (var subDir in subDirs)
            {
                DoScan(subDir);
            }
        }

        static void DoRar(string dir)
        {
            var files = Directory.GetFiles(dir);
            if (files.Length <= 0)
            {
                Console.WriteLine("目录下没有文件: " + dir);
                return;
            }

            var zipFilePath = GetZipFileDir(dir);

            // a表示创建压缩文件 -p表示设置密码 -r表示递归子目录 -ep1表示只保留最后一级目录
            var command = $"\"{RarPath}\" a -psex.main -r -ep1  \"{zipFilePath}\" \"{dir}\\\"";
            using (var writer = new StreamWriter(@"D:\a.bat", true, Encoding.GetEncoding("GB2312")))
            {
                writer.WriteLine(command);
            }

            //            var process = new Process();
            //            process.StartInfo = new ProcessStartInfo("cmd.exe", command);
            //            process.Start();
        }

        static string GetZipFileDir(string zipDir)
        {
            var zipFileDir = Path.GetDirectoryName(zipDir) ?? "";
            var dirName = Path.GetFileName(zipFileDir);
            if (!Regex.IsMatch(dirName, @"^\d+-"))
            {
                dirName = Path.GetFileName(Path.GetDirectoryName(zipFileDir) ?? "");
            }

            if (!zipNames.TryGetValue(zipFileDir, out var num))
            {
                num = 1;
            }

            zipNames[zipFileDir] = num + 1;
            return dirName + "-" + num.ToString("0000") + ".rar";
        }

        static string GetZipFileDir2(string zipDir)
        {
            var zipFile = Path.GetFileName(zipDir) + ".rar";

            var zipFileDir = Path.GetDirectoryName(zipDir) ?? "";
            var dirName = Path.GetFileName(zipFileDir);
            if (!Regex.IsMatch(dirName, @"^\d+-"))
            {
                zipFileDir = Path.GetDirectoryName(zipFileDir) ?? "";
            }

            return Path.Combine(zipFileDir, zipFile);
        }
    }
}
