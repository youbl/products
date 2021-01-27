package beinet.cn.assetmanagement.user;

import beinet.cn.assetmanagement.user.entity.ValidCode;
import beinet.cn.assetmanagement.user.service.ValidCodeService;
import beinet.cn.assetmanagement.utils.ImgHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ImgController
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/27 22:45
 */
@RestController
public class ImgController {
    private final ValidCodeService validCodeService;

    public ImgController(ValidCodeService validCodeService) {
        this.validCodeService = validCodeService;
    }

    /**
     * 获取图形验证码和序号
     *
     * @return 序号和图片
     * @throws IOException 异常
     */
    @GetMapping("img")
    public Map<String, String> imgCode() throws IOException {
        ValidCode code = validCodeService.addCodeAndGetSn();

        Map<String, String> ret = new HashMap<>();
        ImgHelper helper = new ImgHelper();
        ret.put("img", helper.getImageBase64(code.getCode()));
        ret.put("sn", code.getSn());
        return ret;
    }
}
