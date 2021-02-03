package beinet.cn.assetmanagement.assets.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
public class AssetsDto {
    private int id;

    private String code;

    private String assetName;

    private int classId;

    private String description;

    private java.time.LocalDateTime buyTime;

    private int price;

    private int state;

    private String place;

    private java.time.LocalDateTime creationTime;

    private java.time.LocalDateTime lastModificationTime;

    public Assets mapTo() {
        Assets result = new Assets();
        result.setId(this.getId());
        result.setCode(this.getCode());
        result.setAssetName(this.getAssetName());
        result.setClassId(this.getClassId());
        result.setDescription(this.getDescription());
        result.setBuyTime(this.getBuyTime());
        result.setPrice(this.getPrice());
        result.setState(this.getState());
        result.setPlace(this.getPlace());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
}