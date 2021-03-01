package beinet.cn.assetmanagement.assets.model;

import lombok.Data;

@Data
public class AssetSearchDto {

    private String[] account;

    private int[] state;

    private int classId;
}