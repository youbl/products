using System;
using System.Collections.Generic;
using System.Net;
using Beinet.Feign;
using NLog;
using RemindClock.Repository.Model;
using RemindClock.Services;

namespace RemindClock.FeignService
{
    [FeignClient("Sync", Url = "{SyncUrl}", Configuration = typeof(SyncConfig))]
    public interface SyncFeign
    {
        /// <summary>
        /// 获取服务端版本号
        /// </summary>
        [GetMapping("version")]
        int GetServerVersion([RequestParam] string account, [RequestParam] string token);

        /// <summary>
        /// 获取服务端所有提醒
        /// </summary>
        [GetMapping("notes")]
        List<Notes> GetNotes([RequestParam] string account, [RequestParam] string token);

        /// <summary>
        /// 本地所有提醒同步给服务端
        /// </summary>
        [PostMapping("notes")]
        int SaveNotes([RequestParam] string account, [RequestParam] string token, [RequestBody] List<Notes> notes);
    }

    public class SyncConfig : FeignDefaultConfig
    {
        List<IRequestInterceptor> interceptors = new List<IRequestInterceptor>
        {
            new SyncInterceptor()
        };

        public override List<IRequestInterceptor> GetInterceptor()
        {
            return interceptors;
        }
    }

    public class SyncInterceptor : IRequestInterceptor
    {
        private NotesService getNotesService()
        {
            return NotesService.Default;
        }

        public string OnCreate(string originUrl)
        {
            var ret = originUrl.Replace("{SyncUrl}", getNotesService().GetVersion().SyncUrl);
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