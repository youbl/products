package beinet.cn.assetmanagement.logs.service;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.logs.model.OperatelogDto;
import beinet.cn.assetmanagement.logs.repository.OperatelogRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OperatelogService {
    private final OperatelogRepository operatelogRepository;

    public OperatelogService(OperatelogRepository operatelogRepository) {
        this.operatelogRepository = operatelogRepository;
    }

    public Page<Operatelog> findAll(OperatelogDto dto) {
        // 不使用的属性，必须要用 withIgnorePaths 忽略，否则会列入条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "code", "account", "subType", "description", "ip", "creationTime");
        if (null != dto.getType()) {
            matcher = matcher.withMatcher("type", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("type");
        }
        if (StringUtils.hasText(dto.getOperator())) {
            matcher = matcher.withMatcher("operator", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("operator");
        }
        Operatelog condition = Operatelog.builder()
                .type(dto.getType())
                .operator(dto.getOperator())
                .build();
        Example<Operatelog> example = Example.of(condition, matcher);

        int pageNum = Math.max(dto.getPageNum(), 0);
        int pageSize = dto.getPageSize() > 0 && dto.getPageSize() < 100 ? dto.getPageSize() : 12;
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNum, pageSize, direction, "creationTime");
        // getContent是记录，getTotalElements是总记录数，用于前端分页
        return operatelogRepository.findAll(example, pageable);
    }

    public Operatelog findById(Integer id) {
        return operatelogRepository.findById(id).orElse(null);
    }

    public Operatelog save(Operatelog item) {
        if (item == null) {
            return null;
        }
        return operatelogRepository.save(item);
    }

}
