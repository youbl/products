using System.IO;

namespace RemindClock.Utils
{
    public static class FileHelper
    {
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
    }
}