using System;
using System.Collections.Generic;
using System.Configuration;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Threading;
using Beinet.Repository;
using LogAnalyse.LogProcesser.Parsers;
using LogAnalyse.LogProcesser.Repository;
using NLog;

namespace LogAnalyse.LogProcesser
{
    class NginxLogProcesser : IJob
    {
        private static ILogger logger = LogManager.GetCurrentClassLogger();
        private string logFileDir = ConfigurationManager.AppSettings["nginxLogDir"] ?? "";

        // 任务仓储类
        private readonly TasksRepository tasksRepository = ProxyLoader.GetProxy<TasksRepository>();

        // Nginx日志仓储类
        private readonly NginxLogRepository nginxLogRepository = ProxyLoader.GetProxy<NginxLogRepository>();

        private int threadNum = 0;

        private string[] logDir = new[]
        {
            @"172.18.41.131",
            @"172.18.41.132",
            @"172.18.41.193"
        };

        private List<IParser> parserList = new List<IParser>();

        public NginxLogProcesser()
        {
            parserList.Add(new InsertParser());
        }

        public void Run()
        {
            logger.Info("Nginx日志处理启动...");
            var allProcessedTasks = GetTasks();
            logger.Info($"前次已处理任务数：{allProcessedTasks.Count.ToString()}");

            var arrNewFiles = UnzipFiles(allProcessedTasks);
            logger.Info($"本次待处理任务数：{arrNewFiles.Count.ToString()}");

            var processedNumAll = 0;
            foreach (var file in arrNewFiles)
            {
                var taskId = AddTask(file);

                var processedRowNum = ImportFile(file);
                processedNumAll += processedRowNum;

                FinishTask(taskId, processedRowNum);

                DeleteFile(file);
            }

            logger.Info($"本次任务完成，总处理行数：{processedNumAll}");
        }

        private List<string> UnzipFiles(HashSet<string> allProcessedTasks)
        {
            // 只处理昨天的数据
            var day = DateTime.Now.AddDays(-1).ToString("yyyyMMdd");

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
                    if (file.StartsWith("error.log"))
                    {
                        continue;
                    }

                    var txtFile = file.Substring(0, file.Length - 3); // 移除.gz
                    // 不是昨天的日志，或者 已处理
                    if (!txtFile.EndsWith(day) || allProcessedTasks.Contains(txtFile))
                    {
                        DeleteFile(txtFile);
                        continue;
                    }

                    try
                    {
                        if (!File.Exists(txtFile))
                        {
                            DoUnZip(file, Path.GetDirectoryName(txtFile));
                        }
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
            int ret = 0;
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
                    // 计算最大长度
//                    for (var i = 0; i < arrFields.Count; i++)
//                    {
//                        var len = arrFields[i].Length;
//                        if (len > arrLen[i])
//                        {
//                            arrLen[i] = len;
//                            arrVal[i] = arrFields[i];
//                        }
//                    }
                    ret++;
                    Interlocked.Increment(ref threadNum);
                    while (threadNum > 50)
                    {
                        Thread.Sleep(20);
                    }

                    ThreadPool.UnsafeQueueUserWorkItem(state =>
                    {
                        try
                        {
                            foreach (var parser in parserList)
                            {
                                parser.Parse(arrFields, file);
                            }
                        }
                        finally
                        {
                            Interlocked.Decrement(ref threadNum);
                        }
                    }, null);

                    if (ret % 10000 == 0)
                    {
                        logger.Info(file + " 已导入行数:" + ret);
                    }
                }
            }

            logger.Info(file + "导入完成条数 " + ret);
            return ret;
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
                    var field = line.Substring(1, idxEnd - 1);
                    arrField.Add(field);
                }
                else if (ch == '"')
                {
                    idxEnd = line.IndexOf('"', 1);
                    var field = line.Substring(1, idxEnd - 1);
                    arrField.Add(field);
                }
                else
                {
                    idxEnd = line.IndexOf(split, 1);
                    if (idxEnd < 0)
                    {
                        arrField.Add(line);
                        break;
                    }

                    var field = line.Substring(0, idxEnd);
                    arrField.Add(field);
                }

                line = line.Substring(idxEnd + 1).Trim();
            }

            return arrField;
        }

        private int AddTask(string fileName)
        {
            var task = new Tasks();
            task.FileName = fileName;
            task.State = 1;
            return tasksRepository.Save(task).Id;
        }

        private void FinishTask(int id, int insertNum)
        {
            var task = tasksRepository.FindById(id);
            if (task == null)
            {
                logger.Error("未找到任务:" + id);
                return;
            }

            if (task.State != 1)
            {
                logger.Error("任务状态有误:" + id);
                return;
            }

            task.State = 2;
            task.Num = insertNum;
            tasksRepository.Save(task);
        }


        private void DoUnZip(string file, string targetDir)
        {
            if (!targetDir.EndsWith("\\"))
            {
                targetDir += "\\";
            }

            var command = @"C:\Program Files\WinRAR\winrar.exe";
            var args = "x -y " + file + " " + targetDir; // x表示解压 y表示覆盖
            logger.Info(command + " " + args);
            using (var process = Process.Start(command, args))
            {
                process?.WaitForExit();
            }
        }

        private HashSet<string> GetTasks()
        {
            var arr = tasksRepository.findAllFileName();
            return new HashSet<string>(arr);
        }

        private void DeleteFile(String file)
        {
            if (File.Exists(file))
            {
                File.Delete(file);
            }
        }
    }
}