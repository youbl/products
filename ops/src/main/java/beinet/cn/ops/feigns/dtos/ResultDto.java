package beinet.cn.ops.feigns.dtos;

import lombok.Data;

@Data
public class ResultDto<T> {
    private String status;
    private String msg;
    private boolean success;
    private T data;
}
