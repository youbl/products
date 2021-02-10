package beinet.cn.assetmanagement.assets.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
public class AssetauditDto {
    private int id;

    private String type;

    private String subtype;

    private String description;

    private int classId;

    private String auditUser;

    private java.time.LocalDateTime returnTime;

    private String account;

    private int state;

    private String auditReason;

    private java.time.LocalDateTime creationTime;

    private java.time.LocalDateTime lastModificationTime;

    public Assetaudit mapTo() {
        Assetaudit result = new Assetaudit();
        result.setId(this.getId());
        result.setType(this.getType());
        result.setSubtype(this.getSubtype());
        result.setClassId(this.getClassId());
        result.setDescription(this.getDescription());
        result.setAuditUser(this.getAuditUser());
        result.setReturnTime(this.getReturnTime());
        result.setAccount(this.getAccount());
        result.setState(this.getState());
        result.setAuditReason(this.getAuditReason());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
}