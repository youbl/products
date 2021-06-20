using System;
using System.Collections.Generic;
using System.Threading;
using AliyunSDK.Model;

namespace AliyunSDK.Services
{
    /// <summary>
    /// 阿里云API操作基类
    /// </summary>
    public abstract class AliOperation
    {
        protected string AccessKeyId { get; private set; }
        protected string AccessKeySecret { get; private set; }
        public AliOperation(string accessKeyId, string accessKeySecret)
        {
            AccessKeyId = accessKeyId;
            AccessKeySecret = accessKeySecret;
        }
        
        /// <summary>
        /// 有些接口，地域不同，url域名不同，比如slb接口，
        /// 所以要用这个接口访问
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="region"></param>
        /// <param name="serviceCode"></param>
        /// <param name="version"></param>
        /// <param name="param"></param>
        /// <returns></returns>
        protected T AccessAli<T>(string region, string serviceCode, string version, Dictionary<string, string> param)
            where T : class
        {
            var points = GetEndpoint(region, serviceCode);
            var endpoint = points?.Endpoints?.Endpoint;
            if (endpoint == null || endpoint.Count <= 0)
            {
                throw new Exception("获取阿里endpoint失败:" + region + ":" + serviceCode);
            }
            var url = $"http://{endpoint[0].Endpoint}/";  // ecs.aliyuncs.com
            return AccessAli<T>(url, version, param);
        }

        /// <summary>
        /// 获取不同区域的服务终端。
        /// 这个API在阿里的文档里居然找不到
        /// </summary>
        /// <param name="region"></param>
        /// <param name="serviceCode"></param>
        /// <returns></returns>
        private AliEndpoints GetEndpoint(string region, string serviceCode)
        {
            var url = "http://location.aliyuncs.com/";
            var version = "2015-06-12";

            var param = new Dictionary<string, string>();
            param["Action"] = "DescribeEndpoints";
            param["Id"] = region;
            param["ServiceCode"] = serviceCode;
            param["Type"] = "openAPI";
            return AccessAli<AliEndpoints>(url, version, param);
        }


        /// <summary>
        /// 根据参数访问指定url，返回对应的对象数据
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="url"></param>
        /// <param name="version"></param>
        /// <param name="param"></param>
        /// <returns></returns>
        protected T AccessAli<T>(string url, string version, Dictionary<string, string> param)
            where T : class
        {
            var response = AccessAli(url, version, param);
            var ret = Utility.FromJson<T>(response);
            return ret;
        }

        /// <summary>
        /// 根据参数访问指定url，返回对应的对象数据
        /// </summary>
        /// <param name="url"></param>
        /// <param name="version"></param>
        /// <param name="param"></param>
        /// <returns></returns>
        protected string AccessAli(string url, string version, Dictionary<string, string> param)
        {
            // 默认参数加完后，添加公共参数
            /*var signStr = */
            Utility.AddCommonPara(param, version, AccessKeyId, AccessKeySecret);

            url = Utility.AddUrlPara(url, param);
            // 不能重试，因为 SignatureNonce 只能用一次
            return Utility.GetPage(url);
        }
    }

}
