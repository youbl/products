package beinet.cn.assetmanagement.report.dto;

import lombok.Data;

import java.util.List;

@Data
public class TotalDto {
    /**
     * 分类id或state或部门id
     */
    private int key;

    private String name;

    /**
     * 该汇总下所有的明细项汇总
     */
    private List<TotalDto> details;
    /**
     * 数量
     */
    private int num;
}
