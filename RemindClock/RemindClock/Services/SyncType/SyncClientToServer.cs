using Beinet.Feign;
using RemindClock.FeignService;
using RemindClock.Repository;
using RemindClock.Repository.Model;

namespace RemindClock.Services.SyncType
{
    /// <summary>
    /// 客户端同步到服务器的实现
    /// </summary>
    public class SyncClientToServer : ISyncType
    {
        private readonly NotesService notesService = new NotesService();
        private readonly SyncFeign syncFeign = ProxyLoader.GetProxy<SyncFeign>();
        private readonly VersionRepository versionRepository = new VersionRepository();

        public bool Match(Version version, int serverVersion)
        {
            // 服务端版本等于上次同步版本，且本地版本大于服务端版本，走客户端同步
            return (version.ServerVersion == serverVersion
                    && version.ClientVersion > serverVersion);
        }

        public void Sync()
        {
            var allNotes = notesService.FindAll();
            var serverVersion = syncFeign.SaveNotes(SyncService.SyncUser, SyncService.SyncToken, allNotes);

            var version = versionRepository.FindFirst();
            version.ServerVersion = serverVersion;
            version.ClientVersion = serverVersion;
            versionRepository.Save(version);
        }
    }
}