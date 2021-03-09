package beinet.cn.assetmanagement.logs;

import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.OperateEnumDto;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.logs.model.OperatelogDto;
import beinet.cn.assetmanagement.logs.service.OperatelogService;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OperatelogController {
    private final OperatelogService operatelogService;
    private List<OperateEnumDto> enumDtos;

    public OperatelogController(OperatelogService operatelogService) {
        this.operatelogService = operatelogService;
    }

    @GetMapping("/operatelogs")
    public Page<Operatelog> findAll(OperatelogDto dto) {
        return operatelogService.findAll(dto);
    }

    @GetMapping("/operatelog/type")
    public List<OperateEnumDto> logTypeList() {
        if (enumDtos == null) {
            List<OperateEnumDto> ret = new ArrayList<>();
            for (OperateEnum item : OperateEnum.values()) {
                ret.add(OperateEnumDto.builder()
                        .code(item.name())
                        .description(item.getDescription())
                        .build());
            }

            enumDtos = ret;
        }
        return enumDtos;
    }
}
