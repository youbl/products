using System.Collections.Generic;
using LogAnalyse.LogProcesser.Repository;

namespace LogAnalyse.LogProcesser.Parsers
{
    interface IParser
    {
        /// <summary>
        /// 对每行数据的解析处理
        /// </summary>
        /// <param name="ngingLog">行数据</param>
        void Parse(NginxLog ngingLog);

        /// <summary>
        /// 所有行处理完毕时调用的方法
        /// </summary>
        void Finish();
    }
}