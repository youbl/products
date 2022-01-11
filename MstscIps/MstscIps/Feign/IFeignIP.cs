using Beinet.Feign;
using System.Collections.Generic;
using System.Net;
using System;
using MstscIps.Feign.Dto;

namespace MstscIps.Feign
{
    [FeignClient("IP", Url = "http://beinet.cn/", Configuration = typeof(IpConfig))]
    public interface IFeignIP
    {
        /// <summary>
        /// 运行中IP
        /// </summary>
        [GetMapping("")]
        List<VpsMachineDto> GetIP(Uri uri);
    }


    public class IpConfig : FeignDefaultConfig
    {
        List<IRequestInterceptor> interceptors = new List<IRequestInterceptor>
        {
            new IpInterceptor()
        };

        public override List<IRequestInterceptor> GetInterceptor()
        {
            return interceptors;
        }
    }

    public class IpInterceptor : IRequestInterceptor
    {
        public string OnCreate(string originUrl)
        {
            var ret = originUrl; // .Replace("{SyncUrl}", getNotesService().GetVersion().SyncUrl);
            return ret;
        }

        public void BeforeRequest(HttpWebRequest request, string postStr)
        {
        }

        public void AfterRequest(HttpWebRequest request, HttpWebResponse response, string responseStr,
            Exception exception)
        {
        }
    }
}