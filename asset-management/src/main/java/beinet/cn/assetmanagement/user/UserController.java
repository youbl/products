package beinet.cn.assetmanagement.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/27 22:39
 */
@RestController
public class UserController {
    @GetMapping("")
    public String getUser() {
        return "";
    }
}
