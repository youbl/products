using System.Configuration;
using Beinet.Feign;
using RemindClock.FeignService;

namespace RemindClock.Services
{
    class SyncService
    {
        private readonly string SyncUser = ConfigurationManager.AppSettings["SyncUser"] ?? "";
        private readonly string SyncToken = ConfigurationManager.AppSettings["SyncToken"] ?? "";

        private NotesService notesService = new NotesService();
        private SyncFeign syncFeign = ProxyLoader.GetProxy<SyncFeign>();

        public void BeginSync()
        {
            var serverVerNow = syncFeign.GetServerVersion(SyncUser, SyncToken);
        }
    }
}