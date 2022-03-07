using System;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace ConsoleTools.Utilities
{
    internal static class FileHelper
    {
        private static Regex disableDirRegex = new Regex(@"(?i)\\(Windows|ProgramData|Program Files)\\");

        public static void CreateDir(string dir)
        {
            if (!Directory.Exists(dir))
            {
                Directory.CreateDirectory(dir);
            }
        }

        public static void CreateDirByFile(string file)
        {
            CreateDir(Path.GetDirectoryName(file));
        }

        public static void WriteToFile(string file, string content)
        {
            if (string.IsNullOrWhiteSpace(file))
            {
                throw new ArgumentNullException("file不能为空");
            }

            if (disableDirRegex.IsMatch(file))
            {
                throw new ArgumentException("file路径拒绝操作");
            }

            if (File.Exists(file))
            {
                File.Move(file, file + ".bak");
            }
            else
            {
                CreateDirByFile(file);
            }

            using (var sw = new StreamWriter(file, false, Encoding.UTF8))
            {
                sw.Write(content ?? "");
            }
        }
    }
}