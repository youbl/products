using System;
using System.Collections.Generic;
using System.Configuration;
using System.Diagnostics;
using System.IO;
using System.Text;
using Beinet.Repository;
using LogAnalyse.LogProcesser.Repository;
using NLog;

namespace LogAnalyse.LogProcesser
{
    class NginxLog : IJob
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();
        private string logFileDir = ConfigurationManager.AppSettings["nginxLogDir"] ?? "";

        // 仓储类
        private TasksRepository tasksRepository = ProxyLoader.GetProxy<TasksRepository>();

        private string[] logDir = new[]
        {
            @"172.18.41.131",
            @"172.18.41.132",
            @"172.18.41.193"
        };

        public void Run()
        {
            logger.Info("Nginx日志处理启动...");
            var allProcessedTasks = GetTasks();
            logger.Info($"前次已处理任务数：{allProcessedTasks.Count.ToString()}");

            var arrNewFiles = UnzipFiles(allProcessedTasks);
            logger.Info($"本次待处理任务数：{arrNewFiles.Count.ToString()}");

            foreach (var file in arrNewFiles)
            {
                ImportFile(file);
            }
        }

        private List<string> UnzipFiles(HashSet<string> allProcessedTasks)
        {
            var ret = new List<string>();
            foreach (var item in logDir)
            {
                var dir = Path.Combine(logFileDir, item);
                if (!Directory.Exists(dir))
                {
                    logger.Error("指定的目录不存在： " + dir);
                    continue;
                }

                foreach (var file in Directory.GetFiles(dir, "*.gz", SearchOption.TopDirectoryOnly))
                {
                    var txtFile = file.Substring(0, file.Length - 3); // 移除.gz
                    if (allProcessedTasks.Contains(txtFile)) //if (File.Exists(txtFile))
                    {
                        // 已处理
                        continue;
                    }

                    try
                    {
                        DoUnZip(file, Path.GetDirectoryName(txtFile));
                    }
                    catch (Exception exp)
                    {
                        logger.Error(file + "解压失败:" + exp);
                        continue;
                    }

                    ret.Add(txtFile);
                }
            }

            return ret;
        }

        private int ImportFile(string file)
        {
            logger.Info("开始导入 " + file);
            using (var sr = new StreamReader(file, Encoding.UTF8))
            {
                while (!sr.EndOfStream)
                {
                    var line = sr.ReadLine();
                    if (string.IsNullOrEmpty(line))
                    {
                        continue;
                    }

                    var arrFields = ParseLog(line);
                }
            }

            logger.Info("导入完成 " + file);
            return 0;
        }

        private List<string> ParseLog(string line)
        {
            line = line.Trim();
            List<string> arrField = new List<string>();

            var split = ' ';
            while (line.Length > 0)
            {
                var ch = line[0];
                int idxEnd;
                if (ch == '[')
                {
                    idxEnd = line.IndexOf(']', 1);
                }
                else if (ch == '"')
                {
                    idxEnd = line.IndexOf('"', 1);
                }
                else
                {
                    idxEnd = line.IndexOf(split, 1);
                }

                if (idxEnd < 0)
                {
                    arrField.Add(line);
                    break;
                }

                var field = line.Substring(1, idxEnd - 1);
                arrField.Add(field);
                line = line.Substring(idxEnd + 1);
            }

            return arrField;
        }


        private void DoUnZip(string file, string targetDir)
        {
            var command = @"C:\Program Files\WinRAR\winrar.exe";
            var args = "x " + file + " " + targetDir;
            logger.Info(command + " " + args);
            using (var process = Process.Start(command, args))
            {
                process.WaitForExit();
            }
        }

        private HashSet<string> GetTasks()
        {
            var arr = tasksRepository.findAllFileName();
            return new HashSet<string>(arr);
        }
    }
}