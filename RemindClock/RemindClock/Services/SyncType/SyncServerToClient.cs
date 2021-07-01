using System;
using Beinet.Core.Serializer;
using Beinet.Feign;
using NLog;
using RemindClock.FeignService;
using RemindClock.Repository;
using RemindClock.Utils;
using Version = RemindClock.Repository.Model.Version;

namespace RemindClock.Services.SyncType
{
    /// <summary>
    /// 服务器同步到客户端的实现
    /// </summary>
    public class SyncServerToClient : ISyncType
    {
        private static readonly ILogger logger = LogManager.GetCurrentClassLogger();
        private static readonly JsonSerializer serializer = new JsonSerializer();

        private readonly NotesService notesService = NotesService.Default;
        private readonly SyncFeign syncFeign = ProxyLoader.GetProxy<SyncFeign>();
        private readonly VersionRepository versionRepository = new VersionRepository();

        public bool Sync(Version version, int serverVersion)
        {
            // 上次同步版本 < 服务端版本 && 本地版本 == 上次同步版本，走服务端同步
            if (version.ServerVersion < serverVersion
                && version.ClientVersion == version.ServerVersion)
            {
                DoSync(version, serverVersion);
                return true;
            }

            return false;
        }

        private void DoSync(Version version, int serverVersion)
        {
            logger.Info("SyncServerToClient begin: " + version.ServerVersion + ":" + version.ClientVersion);
            var serverNotes = syncFeign.GetNotes(version.SyncUser, version.SyncToken);
            logger.Info("服务端下发数据:\n" + serializer.SerializToStr(serverNotes));

            // 备份本地
            notesService.BackupAll();

            // 保存服务端拉到的提醒
            foreach (var note in serverNotes)
            {
                // 直接使用服务端的id，不再使用客户端ID了
                notesService.Save(note);
            }

            version.ServerVersion = serverVersion;
            version.ClientVersion = serverVersion;
            version.LastSyncTime = DateTime.Now;
            versionRepository.Save(version);
            logger.Info("SyncServerToClient end: " + version.ServerVersion + ":" + version.ClientVersion);

            // 刷新主界面
            FormHelper.Invoke(MainForm.Default, () => MainForm.Default.LoadNotes());
        }
    }
}