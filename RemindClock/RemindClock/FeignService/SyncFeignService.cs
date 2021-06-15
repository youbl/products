using System;
using System.Collections.Generic;
using Beinet.Feign;
using RemindClock.Repository.Model;
using Version = RemindClock.Repository.Model.Version;

namespace RemindClock.FeignService
{
    public class SyncFeignService
    {
        private SyncFeign feign = ProxyLoader.GetProxy<SyncFeign>();

        /// <summary>
        /// 获取服务端版本号
        /// </summary>
        public int GetServerVersion(Version version)
        {
            var uri = GetUri(version, "version");
            return feign.GetServerVersion(version.SyncUser, version.SyncToken, uri);
        }

        /// <summary>
        /// 获取服务端所有提醒
        /// </summary>
        public List<Notes> GetNotes(Version version)
        {
            var uri = GetUri(version, "notes");
            return feign.GetNotes(version.SyncUser, version.SyncToken, uri);
        }

        /// <summary>
        /// 本地所有提醒同步给服务端
        /// </summary>
        public int SaveNotes(Version version, List<Notes> notes)
        {
            var uri = GetUri(version, "notes");
            return feign.SaveNotes(version.SyncUser, version.SyncToken, notes, uri);
        }

        private Uri GetUri(Version version, string route)
        {
            var url = version.SyncUrl;
            if (!url.EndsWith("/"))
                url += "/";
            return new Uri(url + route);
        }
    }
}