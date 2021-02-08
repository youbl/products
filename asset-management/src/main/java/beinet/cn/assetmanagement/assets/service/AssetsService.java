package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assets;
import beinet.cn.assetmanagement.assets.repository.AssetsRepository;
import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.service.UsersService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssetsService {
    private final AssetsRepository assetsRepository;
    private final AssetclassService assetclassService;
    private final UsersService usersService;

    public AssetsService(AssetsRepository assetsRepository,
                         AssetclassService assetclassService,
                         UsersService usersService) {
        this.assetsRepository = assetsRepository;
        this.assetclassService = assetclassService;
        this.usersService = usersService;
    }

    public List<Assets> findAll(String account) {
        Users user = usersService.findByAccount(account, true);
        if (user == null) {
            return new ArrayList<>();
        }
        if (user.isAdmin()) {
            return assetsRepository.findAll();
        }
        return assetsRepository.findAllByAccount(user.getUserName());
    }

    public Assets findById(Integer id) {
        return assetsRepository.findById(id).orElse(null);
    }

    public Assets findByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return assetsRepository.findByCode(code);
    }

    public Assets save(Assets item) {
        if (item == null) {
            return null;
        }
        if (StringUtils.isEmpty(item.getCode())) {
            item.setCode(getCode(item));
        }
        return assetsRepository.save(item);
    }


    /**
     * 8位年月日 + 4位分类 + 6位序号
     *
     * @return
     */
    public String getCode(Assets item) {
        String code = item.getBuyTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                String.format("%04d", item.getClassId()) +
                String.format("%06d", assetclassService.updateAndGetAmount(item.getClassId()));

        return code;
    }
}
