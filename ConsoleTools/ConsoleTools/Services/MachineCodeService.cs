using System;
using System.Text;
using System.Text.RegularExpressions;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    internal class MachineCodeService : ServicesBase
    {
        public const int CODE_LEN = 32;
        private static Regex regInvalidChars = new Regex("[_.-]", RegexOptions.Compiled);


        public string Operate(string[] args)
        {
            var cpuId = SystemHelper.GetCpuId();
            var diskId = SystemHelper.GetDiskId();
            var boardId = SystemHelper.GetBoardId();
            var ret = string.Format("CPUID:{0} 硬盘ID:{1} 主板ID:{2} 机器码:{3}",
                cpuId,
                diskId,
                boardId,
                MakeMachineCode(cpuId, diskId, boardId));

            var saveFile = args.Length > 0 ? args[0] : "";
            if (!string.IsNullOrWhiteSpace(saveFile))
            {
                FileHelper.WriteToFile(saveFile, ret);
            }

            return ret;
        }

        private string MakeMachineCode(params string[] args)
        {
            var sb = new StringBuilder();
            var len = args.Length;
            for (int i = 0, j = len; i < j; i++)
            {
                args[i] = regInvalidChars.Replace(args[i], "");
                sb.Append(args[i]);
            }

            var diff = sb.Length - CODE_LEN;
            if (diff <= 0)
                return sb.ToString();

            sb.Length = 0; // 清空

            // 超过机器码要求的长度时，给每个源，平均减去一定字符数
            var eachSub = (int) Math.Ceiling(diff * 1D / len);
            for (int i = 0, j = len; i < j; i++)
            {
                if (args[i].Length > eachSub)
                    sb.Append(args[i].Substring(eachSub));
                else
                    sb.Append(args[i]);
            }

            if (sb.Length > CODE_LEN)
                sb.Remove(0, sb.Length - CODE_LEN);
            return sb.ToString();
        }
    }
}