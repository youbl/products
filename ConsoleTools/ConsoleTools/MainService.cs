using System.Collections.Generic;
using ConsoleTools.Services;

namespace ConsoleTools
{
    internal class MainService
    {
        private static Dictionary<string, ServicesBase> dictServices;

        static MainService()
        {
            dictServices = new Dictionary<string, ServicesBase>();
            dictServices.Add("1", new CaptureService());
            dictServices.Add("2", new RuntimeService());
            dictServices.Add("3", new ResolutionService());
            dictServices.Add("4", new SupportResolutionService());
            dictServices.Add("5", new ChangeResolutionService());
            dictServices.Add("6", new ServerNameService());
        }

        /// <summary>
        /// 呼叫服务，并把结果存入指定文件
        /// </summary>
        /// <param name="type">服务类型</param>
        /// <param name="saveFile">结果文件，为空表示不写入文件，或使用默认文件</param>
        /// <returns>服务返回的结果</returns>
        public static string CallService(string type, string saveFile)
        {
            if (string.IsNullOrEmpty(type) ||
                !dictServices.TryGetValue(type, out var service))
            {
                return ("未找到服务类，参数: " + type);
            }

            return service.Operate(saveFile);
        }
    }
}