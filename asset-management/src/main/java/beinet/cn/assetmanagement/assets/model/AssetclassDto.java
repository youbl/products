package beinet.cn.assetmanagement.assets.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
public class AssetclassDto {
    private int id;

    private String className;

    private java.time.LocalDateTime creationTime;

    private java.time.LocalDateTime lastModificationTime;

    private String description;

    public Assetclass mapTo() {
        Assetclass result = new Assetclass();
        result.setId(this.getId());
        result.setClassName(this.getClassName());
        result.setDescription(this.getDescription());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
}