using Beinet.Feign;
using RemindClock.FeignService.Dto;

namespace RemindClock.FeignService
{
    [FeignClient("DingDing", Url = "https://oapi.dingtalk.com/")]
    public interface DingDingFeign
    {
        [PostMapping("robot/send")]
        DingDingDto.DingDingResultDto send([RequestParam] string access_token, [RequestBody] DingDingDto dto);
    }
}