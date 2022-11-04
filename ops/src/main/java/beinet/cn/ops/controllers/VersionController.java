package beinet.cn.ops.controllers;

import beinet.cn.ops.feigns.VpsManagerFeign;
import beinet.cn.ops.feigns.dtos.VersionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class VersionController {

    private final VpsManagerFeign vpsManagerFeign;

    @GetMapping("version")
    public VersionDto getLatestVersion(@RequestParam String identify, @RequestParam String env) throws URISyntaxException {
        String domain;
        switch (env) {
            case "test":
                domain = "https://test-rpascheduler.ziniao.com";
                break;
            case "prev":
                domain = "https://pre-rpascheduler.ziniao.com";
                break;
            case "prod":
                domain = "https://rpascheduler.ziniao.com";
                break;
            default:
                throw new RuntimeException("不支持的环境:" + env);
        }
        VersionDto dto = new VersionDto();
        dto.setIdentify(identify);
        return vpsManagerFeign.getLatest(new URI(domain), dto).getData();
    }
}
