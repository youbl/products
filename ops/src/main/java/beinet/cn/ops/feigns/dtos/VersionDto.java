package beinet.cn.ops.feigns.dtos;

import lombok.Data;

@Data
public class VersionDto {
    private String identify;

    private String updateDate;
    private String updateUser;
    private String version;
    private String url;
    private String md5;
    private Long fileSize;
    private String extra;
    private String remark;
}
