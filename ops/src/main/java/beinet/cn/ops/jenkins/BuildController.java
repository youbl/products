package beinet.cn.ops.jenkins;

import beinet.cn.ops.jenkins.dtos.ProjectBuildDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BuildController {
    private final JekinsService jekinsService;

    @PostMapping("build")
    public String startBuild(ProjectBuildDto dto) {
        jekinsService.startBuild(dto);
        return "";
    }
}
