using RemindClock.Repository.Model;

namespace RemindClock.Services.SyncType
{
    /// <summary>
    /// 服务器同步到客户端的实现
    /// </summary>
    public class SyncServerToClient : ISyncType
    {
        private readonly NotesService notesService = new NotesService();

        public bool Match(Version version, int serverVersion)
        {
            // 上次同步版本 < 服务端版本 && 本地版本 == 上次同步版本，走服务端同步
            return (version.ServerVersion < serverVersion
                    && version.ClientVersion == version.ServerVersion);
        }

        public void Sync()
        {
        }
    }
}