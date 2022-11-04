package beinet.cn.ops.jenkins.dtos;

import lombok.Data;

@Data
public class ProjectBuildDto {

    private String jenkinsName;
    private String pack_env;
    private String publish_branch;
    private String is_publish;
    private String version_desc;

    /**
     * 为true时要先清理工作空间
     */
    private boolean clean;
    /**
     * 为true时要进行代码检查
     */
    private boolean sonar;
}
