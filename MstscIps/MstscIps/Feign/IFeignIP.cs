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

        /*
         返回数据格式：
[
    {
        "id": "123",
        "createDate": "2022-03-08T16:32:15",
        "updateDate": "2022-03-08T16:32:15",
        "statusCd": "1000",
        "vpsIp": "1.2.3.7",
        "vpsStatus": "running",
        "openDateTime": "2022-03-08T16:32:15",
        "groupCode": "NORMAL_GROUP",
        "vpsOpenDuration": "0",
        "imageName": "aws-java-pro",
        "instanceId": "i-abcd"
    }
]
         */
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