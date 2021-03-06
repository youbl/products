package beinet.cn.assetmanagement.assets;

import beinet.cn.assetmanagement.assets.model.Assets;
import beinet.cn.assetmanagement.assets.model.AssetsDto;
import beinet.cn.assetmanagement.assets.model.AssetsSearchDto;
import beinet.cn.assetmanagement.assets.service.AssetsService;
import beinet.cn.assetmanagement.event.EventDto;
import beinet.cn.assetmanagement.event.Publisher;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.security.AuthDetails;
import beinet.cn.assetmanagement.utils.ExcelOperator;
import beinet.cn.assetmanagement.utils.RequestHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
public class AssetsController {
    private final AssetsService assetsService;
    private final ExcelOperator excelOperator;

    public AssetsController(AssetsService assetsService,
                            ExcelOperator excelOperator) {
        this.assetsService = assetsService;
        this.excelOperator = excelOperator;
    }

    @GetMapping("/assets")
    public List<Assets> findAll(AssetsSearchDto dto, AuthDetails details) {
        return assetsService.findAll(dto, details.getAccount());
    }

    @GetMapping("/assets/export")
    public void exportAll(AuthDetails details, HttpServletResponse response) throws Exception {
        List<List<String>> result = assetsService.getExcelData(details.getAccount());

        response.addHeader("Content-Disposition", "attachment; filename=\"assets.xlsx\"");
        excelOperator.writeExcel(response.getOutputStream(), result);
    }

    @GetMapping("/assets/mine")
    public List<Assets> findByCurrentUser(@RequestParam(required = false) String export,
                                          AuthDetails details, HttpServletResponse response) throws Exception {
        List<Assets> ret = assetsService.findByUser(details.getAccount());
        if (StringUtils.hasText(export)) {
            List<List<String>> result = assetsService.getExcelData(ret);
            response.addHeader("Content-Disposition", "attachment; filename=\"assetsMine.xlsx\"");
            excelOperator.writeExcel(response.getOutputStream(), result);
        }
        return ret;
    }

    @GetMapping("/assets/user/{account}")
    public List<Assets> findByUser(@PathVariable String account) {
        return assetsService.findByUser(account);
    }

    @GetMapping("asset")
    public Assets findById(Integer id) {
        return assetsService.findById(id);
    }

    @PostMapping("asset")
    public Assets save(@RequestBody AssetsDto item,
                       AuthDetails details,
                       HttpServletRequest request) {
        if (item == null) {
            return null;
        }
        Assets ret = assetsService.save(item.mapTo());

        // 发事件
        Publisher.publishEvent(EventDto.builder()
                .type((item.getId() > 0) ? OperateEnum.EditAsset : OperateEnum.AddAsset)
                .account(details.getAccount())
                .ip(RequestHelper.getFullIP(request))
                .source(item)
                .build());
        return ret;
    }

    /**
     * 扫码接口，不需要登录
     *
     * @param code 二维码
     * @return 资产
     */
    @GetMapping("/assetCode/{code}")
    public AssetsDto findByCode(@PathVariable String code) {
        return assetsService.findDtoByCode(code);
    }

    @PostMapping("/asset/import")
    public List<String> importAssets(@RequestParam MultipartFile file) throws Exception {
        if (file == null || file.getSize() <= 100) {
            throw new RuntimeException("空文件上传了");
        }
        String uploadFileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(uploadFileName)) {
            throw new RuntimeException("文件名为空");
        }
        uploadFileName = uploadFileName.toLowerCase(Locale.ROOT);
        if (!uploadFileName.endsWith(".xls") && !uploadFileName.endsWith(".xlsx")) {
            throw new RuntimeException("只允许excel扩展名");
        }
        if (file.getSize() > 1024 * 1024 * 2) {
            throw new RuntimeException("文件不允许超出2M");
        }

        return assetsService.doImport(file.getInputStream());
    }

    /**
     * 读取指定的模板文件内容，并提供下载
     * 注：没必要，直接用 /template/assets.xlsx 下载就好了
     *
     * @param response 响应流
     * @throws IOException 异常
     */
    @GetMapping("/assets/template")
    public void getTemplate(HttpServletResponse response) throws IOException {
        String filename = "assets.xlsx";
        Resource resource = new ClassPathResource("static/template/" + filename);
        byte[] buffer;
        // this.getClass().getResourceAsStream("/template/assets.xlsx")
        try (InputStream is = resource.getInputStream()) {
            buffer = new byte[is.available()];
            is.read(buffer);
        }

//        response.reset();
//        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.getOutputStream().write(buffer);
    }
}
