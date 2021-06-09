using Beinet.Feign;

namespace RemindClock.FeignService
{
    // 读取配置里的 SyncUrl
    [FeignClient("Sync", Url = "{SyncUrl}")]
    public interface SyncFeign
    {
        [GetMapping("version")]
        int GetServerVersion([RequestParam] string account, [RequestParam] string token);
    }
}