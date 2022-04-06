using System;
using System.Text;
using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    internal class MachineCodeService : ServicesBase
    {
        public const int CODE_LEN = 32;

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
                sb.Append(args[i].Substring(eachSub));
            }

            return sb.ToString();
        }
    }
}