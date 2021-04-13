using System;
using System.Collections.Generic;
using System.Configuration;
using System.Reflection;
using System.Threading;

namespace LogAnalyse
{
    class JobOperator
    {
        private string[] _arrClassName = (ConfigurationManager.AppSettings["RunClassName"] ?? "").Split(',', ';');

        /// <summary>
        /// 匹配当前库中所有要启动的IJob类
        /// </summary>
        /// <returns></returns>
        public List<ThreadStart> GetIJobClass()
        {
            var ret = new List<ThreadStart>();

            var ijob = typeof(IJob);
            var assembly = Assembly.GetExecutingAssembly();
            foreach (var type in assembly.GetTypes())
            {
                if (ijob.IsAssignableFrom(type) && IsConfigedClass(type)) // type.IsSubclassOf(ijob)
                {
                    if (assembly.CreateInstance(type.FullName ?? "") is IJob instance)
                    {
                        ret.Add(instance.Run);
                    }
                }
            }

            return ret;
        }


        /// <summary>
        /// 配置里是否要启动当前Type
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        private bool IsConfigedClass(Type type)
        {
            var name = type.FullName ?? "";
            foreach (var runClassName in _arrClassName)
            {
                if (!string.IsNullOrWhiteSpace(runClassName) &&
                    name.EndsWith(runClassName, StringComparison.OrdinalIgnoreCase))
                {
                    return true;
                }
            }

            return false;
        }
    }
}