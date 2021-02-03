package beinet.cn.assetmanagement.logs.service;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.logs.repository.OperatelogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatelogService {
    private final OperatelogRepository operatelogRepository;

    public OperatelogService(OperatelogRepository operatelogRepository) {
        this.operatelogRepository = operatelogRepository;
    }

    public List<Operatelog> findAll() {
        return operatelogRepository.findAll();
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
