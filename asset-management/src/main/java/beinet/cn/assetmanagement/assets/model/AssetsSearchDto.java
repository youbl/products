package beinet.cn.assetmanagement.assets.model;

import lombok.Data;

@Data
public class AssetsSearchDto {

    private String code;

    private String assetName;

    private int classId;

    private String className;

    private String description;

    private int price;

    private Integer[] state;

    private String place;
}