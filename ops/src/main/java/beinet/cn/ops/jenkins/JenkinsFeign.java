package beinet.cn.ops.jenkins;

import beinet.cn.ops.jenkins.dtos.JenkinsBuildInfo;
import feign.*;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

@FeignClient(value = "jenkins", url = "http://10.100.72.165:8080/", configuration = JenkinsFeign.MultipartSupportConfig.class)
public interface JenkinsFeign {
    // 开始部署
    @RequestLine("POST /job/{jenkinsName}/buildWithParameters")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ResponseEntity<String> startDeploy(
            @Param("jenkinsName") String jenkinsName,
            @Param("pack_env") String pack_env,
            @Param("publish_branch") String publish_branch,
            @Param("is_publish") String is_publish,
            @Param("version_desc") String version_desc
    );

    /**
     * 获取某一次构建的进度文本信息
     *
     * @param jenkinsName JOB名
     * @param buildNum    具体的构建序列号，可以是lastBuild，表示最后一次构建。
     *                    注：如果还在排队，得到的是前一次的构建
     * @param start
     * @return
     */
    @RequestLine("GET /job/{jenkinsName}/{buildNum}/logText/progressiveHtml?start={start}")
    String getBuildProgressive(@Param("jenkinsName") String jenkinsName,
                               @Param("buildNum") int buildNum,
                               @Param("start") int start);

    /**
     * 获取某一次构建的进度信息
     *
     * @param jenkinsName JOB名
     * @param number      具体的构建序列号，可以是lastBuild，表示最后一次构建。
     *                    *                 注：如果还在排队，得到的是前一次的构建
     * @return
     */
    @RequestLine("GET /job/{jenkinsName}/{number}/api/json?tree=*,executor[*],actions[parameters[*]]")
    JenkinsBuildInfo getBuildInfo(
            @Param("jenkinsName") String jenkinsName, @Param("number") int number);


    // 新建任务，得到的只是排队id，不是BuildNum
    @RequestLine("GET /queue/item/{queueId}/api/json")
    HashMap<String, Object> queueItem(@Param("queueId") String queueId);

    // 调用清理工作空间的job（创建一个job叫 ClearWorkSpace）
    @RequestLine("POST /job/ClearWorkSpace/buildWithParameters")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ResponseEntity<String> clearWorkSpace(
            @Param("clearjobname") String clearJobName);

    // 获取job的配置，可以用于备份
    @RequestLine("GET /job/{jenkinsName}/config.xml")
    String getJobConfig(@Param("jenkinsName") String jenkinsName);


    // 创建job
    @RequestLine("POST /createItem?name={jenkinsName}")
    @Headers("Content-Type: application/xml")
    void createJob(@Param("jenkinsName") String jenkinsName, String config);

    // 删除job
    @RequestLine("POST /job/{jenkinsName}/doDelete")
    void deleteJob(@Param("jenkinsName") String jenkinsName);

    // 更新job的配置
    @RequestLine("POST /job/{jenkinsName}/config.xml")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    void updateJobConfig(@Param("jenkinsName") String jenkinsName, String config);


    class MultipartSupportConfig {
        @Value("${jenkins.user:beinet}")
        private String jenkinsUser;
        @Value("${jenkins.password:123456}")
        private String jenkinsPassword;
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Contract feignContract() {
            return new feign.Contract.Default();
        }

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }

        @Bean
        public BasicAuthRequestInterceptor basicAuthorizationInterceptor() {
            return new BasicAuthRequestInterceptor(jenkinsUser, jenkinsPassword);
        }

        @Bean
        public Logger.Level level() {
            return Logger.Level.FULL;
        }
    }
}
