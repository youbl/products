package beinet.cn.ops.feigns;

import beinet.cn.ops.feigns.dtos.ResultDto;
import beinet.cn.ops.feigns.dtos.VersionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;

@FeignClient(value = "vpsManager", url = "https://test-rpascheduler.ziniao.com")
public interface VpsManagerFeign {
    @PostMapping(value = "/vps-manage-service/worker/version/latest",
            headers = {"simple-auth=vps-executor:zx2021RpaScheduler"})
    ResultDto<VersionDto> getLatest(URI baseUrl, VersionDto appInfo);
}
