package beinet.cn.assetmanagement.user;

import beinet.cn.assetmanagement.user.model.Validcode;
import beinet.cn.assetmanagement.user.service.ValidcodeService;
import beinet.cn.assetmanagement.utils.ImgHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ValidcodeController {
    private final ValidcodeService validcodeService;

    public ValidcodeController(ValidcodeService validcodeService) {
        this.validcodeService = validcodeService;
    }

    @GetMapping("validcodes")
    public List<Validcode> findAll() {
        return validcodeService.findAll();
    }

    @GetMapping("validcode")
    public Validcode findById(Integer id) {
        return validcodeService.findById(id);
    }

    @PostMapping("validcode")
    public Validcode save(@RequestBody Validcode item) {
        if (item == null) {
            return null;
        }
        return validcodeService.save(item);
    }

    /**
     * 获取图形验证码和序号
     *
     * @return 序号和图片
     * @throws IOException 异常
     */
    @GetMapping("validcode/img")
    public Map<String, String> imgCode() throws IOException {
        Validcode code = validcodeService.addCodeAndGetSn();

        Map<String, String> ret = new HashMap<>();
        ImgHelper helper = new ImgHelper();
        ret.put("img", helper.getImageBase64(code.getCode()));
        ret.put("sn", code.getSn());
        return ret;
    }
}
