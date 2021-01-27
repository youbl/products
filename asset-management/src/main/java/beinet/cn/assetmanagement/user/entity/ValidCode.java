package beinet.cn.assetmanagement.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 用于校验的验证码
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/18 11:46
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 验证码
     */
    private String code;
    /**
     * 序号
     */
    private String sn;
    /**
     * 类型，0图形验证码，1为短信验证码
     */
    private int type;
    /**
     * 允许错误次数，为0表示作废，不再使用.
     * 图形验证码初始值为1，短信为3
     */
    private int enableErrNum;

    /**
     * 创建时间
     */
    private LocalDateTime creationTime;
}
