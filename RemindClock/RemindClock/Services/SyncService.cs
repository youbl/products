using System;
using System.Collections.Generic;
using System.Configuration;
using Beinet.Feign;
using NLog;
using RemindClock.FeignService;
using RemindClock.Repository;
using RemindClock.Services.SyncType;
using Version = RemindClock.Repository.Model.Version;

namespace RemindClock.Services
{
    class SyncService
    {
        private static readonly ILogger logger = LogManager.GetCurrentClassLogger();

        private static readonly bool SyncDisabled =
            (ConfigurationManager.AppSettings["SyncEnable"] ?? "true").ToLower() != "true";

        private static readonly string SyncUser = ConfigurationManager.AppSettings["SyncUser"] ?? "";
        private static readonly string SyncToken = ConfigurationManager.AppSettings["SyncToken"] ?? "";

        private readonly SyncFeign syncFeign = ProxyLoader.GetProxy<SyncFeign>();
        private readonly VersionRepository versionRepository = new VersionRepository();
        private readonly List<ISyncType> syncTypeList = new List<ISyncType>();

        public SyncService()
        {
            syncTypeList.Add(new SyncServerToClient());
            syncTypeList.Add(new SyncClientToServer());
            syncTypeList.Add(new SyncMerge());
        }

        public void BeginSync()
        {
            if (SyncDisabled)
                return;

            try
            {
                StartSync();
            }
            catch (Exception exp)
            {
                logger.Error(exp, "同步失败");
            }
        }

        private void StartSync()
        {
            // 读取服务端版本号
            var serverVerNow = syncFeign.GetServerVersion(SyncUser, SyncToken);

            // 读取本地版本号 和 上次同步的服务端版本号
            var verObj = versionRepository.FindFirst() ?? new Version();

            VersionCheck(verObj, serverVerNow);

            foreach (var syncType in syncTypeList)
            {
                if (syncType.Match(verObj, serverVerNow))
                {
                    syncType.Sync();
                    return;
                }
            }
        }

        private void VersionCheck(Version verObj, int serverVerNow)
        {
            if (verObj.ClientVersion < verObj.ServerVersion)
            {
                throw new ArgumentException(
                    $"本地版本:{verObj.ClientVersion.ToString()} 小于 上次同步版本:{verObj.ServerVersion.ToString()}， 有问题");
            }

            if (verObj.ServerVersion > serverVerNow)
            {
                throw new ArgumentException(
                    $"服务端版本:{serverVerNow.ToString()} 小于 上次同步版本:{verObj.ServerVersion.ToString()}， 有问题");
            }
        }
    }
}