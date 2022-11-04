package beinet.cn.ops.jenkins.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class JenkinsBuildInfo {
    private boolean building;
    private String displayName;
    private int duration;
    private int estimatedDuration;
    private int number;
    private int queueId;
    private String result;
    private String url;
    private Executor executor;
    private Map<String, Object> parameters = new HashMap<>();
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Map> actions;

    @Data
    public static class Executor {
        private int number;
        private int progress;
    }

    public void updateParameters() {
        if (actions == null)
            return;
        for (Map action : actions) {
            if ("hudson.model.ParametersAction".equals(action.get("_class"))) {
                List<Map<String, String>> items = (List<Map<String, String>>) action.get("parameters");
                for (Map<String, String> item : items) {
                    parameters.put(item.get("name"), item.get("value"));
                }
            }
        }
    }
}