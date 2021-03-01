package beinet.cn.assetmanagement.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersSearchDto {
    private String code;

    private String account;

    private String userName;

    private Integer[] state;
}