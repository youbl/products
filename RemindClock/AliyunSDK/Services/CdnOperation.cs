using System;
using System.Collections.Generic;
using AliyunSDK.Model;

namespace AliyunSDK.Services
{
    /// <summary>
    /// 阿里云的CDN操作类
    /// </summary>
    public class CdnOperation : AliOperation
    {
        public CdnOperation(string accessKeyId, string accessKeySecret) : base(accessKeyId, accessKeySecret)
        {
        }

        /// <summary>
        /// 刷新cdn接口.
        /// 官方参数参考：https://help.aliyun.com/document_detail/91164.html
        /// </summary>
        /// <param name="objectPath">输入示例：abc.com/image/1.png，多个URL之间需要用换行符（\n或\r\n）分隔</param>
        /// <param name="objectType">可以为File或Directory，默认是File。</param>
        /// <returns></returns>
        public string RefreshCaches(string objectPath, string objectType = "File")
        {
            if (string.IsNullOrEmpty(objectPath))
                throw new ArgumentException("对象路径不能为空", nameof(objectPath));
            if(objectType != "File" && objectType != "Directory")
                throw new ArgumentException("对象类型只能是File或Directory", nameof(objectType));

            var url = "http://cdn.aliyuncs.com/";
            var version = "2018-05-10";

            var param = new Dictionary<string, string>();
            param["Action"] = "RefreshObjectCaches";
            param["ObjectPath"] = objectPath;
            param["ObjectType"] = objectType;
            return AccessAli(url, version, param);
        }
        
    }
}
