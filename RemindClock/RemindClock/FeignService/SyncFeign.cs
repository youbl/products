using System;
using System.Collections.Generic;
using Beinet.Feign;
using RemindClock.Repository.Model;

namespace RemindClock.FeignService
{
    [FeignClient("Sync", Url = "http://beinet.cn")]
    public interface SyncFeign
    {
        /// <summary>
        /// 获取服务端版本号
        /// </summary>
        [GetMapping("version")]
        int GetServerVersion([RequestParam] string account, [RequestParam] string token, Uri uri);

        /// <summary>
        /// 获取服务端所有提醒
        /// </summary>
        [GetMapping("notes")]
        List<Notes> GetNotes([RequestParam] string account, [RequestParam] string token, Uri uri);

        /// <summary>
        /// 本地所有提醒同步给服务端
        /// </summary>
        [PostMapping("notes")]
        int SaveNotes([RequestParam] string account, [RequestParam] string token, [RequestBody] List<Notes> notes, Uri uri);
    }
}