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

        private int threadNum = 0;

        private string[] logDir = new[]
        {
            @"172.18.41.131",
            @"172.18.41.132",
            @"172.18.41.193"
        };

        // 文件名包含这些内容，不处理
        private string[] ignoreFiles = new[]
        {
            "error.log",
            "agent.",
            "api.",
            "callback."
        };

        private List<IParser> parserList = new List<IParser>();

        public NginxLogProcesser()
        {
            // parserList.Add(new InsertParser());
            // parserList.Add(new GroupReferAndPathParser());
            parserList.Add(new GroupAppParser());
        }

        public void Run()
        {
            var today = DateTime.Now.Date;
            var start = today.AddDays(-3); // 处理最近3天
            while (start <= today)
            {
                RunWithYmd(start.ToString("yyyyMMdd"));
                start = start.AddDays(1);
            }
        }

        private void RunWithYmd(string ymd)
        {
            logger.Info($"{ymd} Nginx日志处理启动...");

            var allProcessedTasks = GetTasks(ymd);
            logger.Info($"{ymd} 前次已处理任务数：{allProcessedTasks.Count.ToString()}");


            var arrNewFiles = UnzipFiles(allProcessedTasks, ymd);
            logger.Info($"{ymd} 本次待处理任务数：{arrNewFiles.Count.ToString()}");
            if (arrNewFiles.Count <= 0)
                return;

            var processedNumAll = 0;
            foreach (var file in arrNewFiles)
            {
                var taskId = AddTask(file, ymd);

                var processedRowNum = ImportFile(file, ymd);
                processedNumAll += processedRowNum;

                FinishTask(taskId, processedRowNum);
            }

            logger.Info($"{ymd} 导入文件任务，准备最后一步……{processedNumAll}");

            foreach (var parser in parserList)
            {
                parser.Finish(ymd);
            }

            logger.Info($"{ymd} Finish工作完成，准备清理文件:{arrNewFiles.Count}个");

            foreach (var file in arrNewFiles)
                DeleteFile(file);

            logger.Info($"{ymd} 本次任务完成，总处理行数：{processedNumAll}");
        }

        private bool NeedIgnore(string file)
        {
            foreach (var ignoreFile in ignoreFiles)
            {
                if (file.Contains(ignoreFile))
                {
                    return true;
                }
            }

            return false;
        }

        /// <summary>
        /// 解压一天的文件，约3~4分钟
        /// </summary>
        /// <param name="allProcessedTasks"></param>
        /// <param name="day"></param>
        /// <returns></returns>
        private List<string> UnzipFiles(HashSet<string> allProcessedTasks, string day)
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
                    if (NeedIgnore(file))
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

        /// <summary>
        /// 汇总一天的数据，GroupAppParser执行约3~4分钟
        /// </summary>
        /// <param name="file"></param>
        /// <param name="ymd"></param>
        /// <returns></returns>
        private int ImportFile(string file, String ymd)
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

                    ret++;
                    // 最多队列数限制，避免溢出
                    Interlocked.Increment(ref threadNum);
                    while (threadNum > 50)
                    {
                        Thread.Sleep(20);
                    }

                    var arrFields = ParseLog(line);
                    var nginxLog = ConvertToNginxLog(arrFields, file);
//                    if (nginxLog.Time.Day != 20)
//                    {
//                        logger.Error(line);
//                    }

                    ThreadPool.UnsafeQueueUserWorkItem(state =>
                    {
                        try
                        {
                            foreach (var parser in parserList)
                            {
                                parser.Parse(nginxLog);
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

            // 等待所有线程完成
            while (threadNum > 0)
            {
                Thread.Sleep(20);
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

        private NginxLog ConvertToNginxLog(List<string> arrFields, string fileName)
        {
            var log = new NginxLog();
            int tmp;
            double tmpd;

            log.Timelocal = arrFields[0]; // '时间',
            log.Remoteaddr = arrFields[1]; // 'ip',
            log.Remoteuser = arrFields[2]; // '用户',
            log.Host = arrFields[3]; // '主机',
            log.Request = arrFields[4]; // '请求方法和地址',

            int.TryParse(arrFields[5], out tmp);
            log.Status = tmp; // '响应状态',
            int.TryParse(arrFields[6], out tmp);
            log.Requestlength = tmp; // '请求长度',
            int.TryParse(arrFields[7], out tmp);
            log.Bodybytessent = tmp; // '发送长度',
            log.Referer = arrFields[8]; // 'referer',
            log.Useragent = arrFields[9]; // 'ua',
            log.Forwardedfor = arrFields[10]; // '代理ip',
            log.Upstreamaddr = arrFields[11]; // '后端ip+端口',
            double.TryParse(arrFields[12], out tmpd);
            log.Requesttime = (int) Math.Floor(tmpd * 1000); // '请求时长',
            double.TryParse(arrFields[13], out tmpd);
            log.Upstreamtime = (int) Math.Floor(tmpd * 1000); // '后端响应时长',
            int.TryParse(arrFields[14], out tmp);
            log.Upstreamstatus = tmp; // '后端状态',
            int.TryParse(arrFields[15], out tmp);
            log.Contentlength = tmp; // '内容长度',
            int.TryParse(arrFields[16], out tmp);
            log.Httpcontentlength = tmp; // 'http内容长',
            int.TryParse(arrFields[17], out tmp);
            log.Sentcontentlength = tmp; // '发送内容长',
            log.Filename = fileName; // '采集源文件',
            return log;
        }

        private int AddTask(string fileName, string ymd)
        {
            var task = new Tasks();
            task.FileName = fileName;
            task.Ymd = ymd;
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

        private HashSet<string> GetTasks(string ymd)
        {
            var arr = tasksRepository.findAllFileName(ymd);
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