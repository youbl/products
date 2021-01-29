package beinet.cn.assetmanagement.user.model;

import lombok.Data;

@Data
public class ValidcodeDto {
    private int id;

    private String code;

    private String sn;

    private int type;

    private int enableErrNum;

    private java.time.LocalDateTime creationTime;

    private java.time.LocalDateTime lastModificationTime;

    private String description;

    public Validcode mapTo() {
        Validcode result = new Validcode();
        result.setId(this.getId());
        result.setCode(this.getCode());
        result.setSn(this.getSn());
        result.setType(this.getType());
        result.setEnableErrNum(this.getEnableErrNum());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
}