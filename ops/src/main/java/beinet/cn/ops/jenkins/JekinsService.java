package beinet.cn.ops.jenkins;

import beinet.cn.ops.jenkins.dtos.ProjectBuildDto;
import beinet.cn.ops.utils.ThreadHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JekinsService {
    private final JenkinsFeign jenkinsFeign;

    public int startBuild(ProjectBuildDto dto) {
        ResponseEntity entity = jenkinsFeign.startDeploy(dto.getJenkinsName(),
                dto.getPack_env(),
                dto.getPublish_branch(),
                dto.getIs_publish(),
                dto.getVersion_desc());
        return getNumber(entity);
    }

    // 启动构建时，返回的是排队ID，要等到Jenkins启动后，才会返回BuildNum构建序列号
    private int getNumber(ResponseEntity response) {
        String queueId = getQueueId(response);
        return getNumber(queueId);
    }

    private String getQueueId(ResponseEntity response) {
        String location = response.getHeaders().getFirst("location");
        String[] split = location.split("/");
        return split[split.length - 1];
    }

    // 根据Jenkins的QueueId，查找执行中的TaskId，如果任务处于Pending状态时，会返回0
    public int getNumber(String queueId) {
        int times = 0;
        while (times < 10) {
            ThreadHelper.sleep(1000);
            HashMap<String, Object> map = jenkinsFeign.queueItem(queueId);
            Object objExe = map.get("executable");
            if (objExe != null) {
                Object ret = ((Map<String, Object>) objExe).get("number");
                if (ret != null)
                    return (int) ret;
            }
            times++;
        }
        return 0;
    }
}
