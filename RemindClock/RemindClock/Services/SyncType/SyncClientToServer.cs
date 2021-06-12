using System;
using Beinet.Feign;
using NLog;
using RemindClock.FeignService;
using RemindClock.Repository;
using Version = RemindClock.Repository.Model.Version;

namespace RemindClock.Services.SyncType
{
    /// <summary>
    /// 客户端同步到服务器的实现
    /// </summary>
    public class SyncClientToServer : ISyncType
    {
        private static readonly ILogger logger = LogManager.GetCurrentClassLogger();

        private readonly NotesService notesService = new NotesService();
        private readonly SyncFeign syncFeign = ProxyLoader.GetProxy<SyncFeign>();
        private readonly VersionRepository versionRepository = new VersionRepository();

        public bool Sync(Version version, int serverVersion)
        {
            // 服务端版本等于上次同步版本，且本地版本大于服务端版本，走客户端同步
            if (version.ServerVersion == serverVersion
                && version.ClientVersion > serverVersion)
            {
                DoSync(version);
                return true;
            }

            return false;
        }

        private void DoSync(Version version)
        {
            logger.Info("SyncClientToServer begin: " + version.ServerVersion + ":" + version.ClientVersion);
            var allNotes = notesService.FindAll();
            var serverVersion = syncFeign.SaveNotes(SyncService.SyncUser, SyncService.SyncToken, allNotes);

            version.ServerVersion = serverVersion;
            version.ClientVersion = serverVersion;
            version.LastSyncTime = DateTime.Now;
            versionRepository.Save(version);
            logger.Info("SyncClientToServer end: " + version.ServerVersion + ":" + version.ClientVersion);
        }
    }
}