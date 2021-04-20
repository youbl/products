using System.Collections.Generic;

namespace LogAnalyse.LogProcesser.Parsers
{
    interface IParser
    {
        /// <summary>
        /// 对每行数据的解析处理
        /// </summary>
        /// <param name="arrFields">行数据</param>
        /// <param name="fileName">所属文件名</param>
        void Parse(List<string> arrFields, string fileName);
    }
}