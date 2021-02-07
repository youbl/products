package beinet.cn.assetmanagement.assets.model;

import lombok.*;

@Data
public class AssetclassDto {
    private int id;

    private String className;

    private java.time.LocalDateTime creationTime;

    private java.time.LocalDateTime lastModificationTime;

    private String description;

    private String admin;

    public Assetclass mapTo() {
        Assetclass result = new Assetclass();
        result.setId(this.getId());
        result.setClassName(this.getClassName());
        result.setAdmin(this.getAdmin());
        result.setDescription(this.getDescription());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
}