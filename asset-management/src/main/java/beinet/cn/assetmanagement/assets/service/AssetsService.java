package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assetclass;
import beinet.cn.assetmanagement.assets.model.Assets;
import beinet.cn.assetmanagement.assets.model.AssetsDto;
import beinet.cn.assetmanagement.assets.repository.AssetsRepository;
import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.service.UsersService;
import beinet.cn.assetmanagement.utils.ExcelOperator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetsService {
    private final AssetsRepository assetsRepository;
    private final AssetclassService assetclassService;
    private final UsersService usersService;

    private final ExcelOperator excelOperator;

    public AssetsService(AssetsRepository assetsRepository,
                         AssetclassService assetclassService,
                         UsersService usersService,
                         ExcelOperator excelOperator) {
        this.assetsRepository = assetsRepository;
        this.assetclassService = assetclassService;
        this.usersService = usersService;
        this.excelOperator = excelOperator;
    }

    public List<Assets> findAll(Integer state, String account) {
        Users user = usersService.findByAccount(account, true);
        if (user == null) {
            return new ArrayList<>();
        }
        List<Assets> result;
        if (user.isAdmin()) {
            result = assetsRepository.findAll();
        } else {
            result = assetsRepository.findAllByAdminAccount(user.getUserName());
        }
        if (state != null) {
            result = result.stream().filter(assets -> assets.getState() == state).
                    collect(Collectors.toList());
        }
        return result;
    }

    public List<Assets> findByCurrentUser(String account) {
        Users user = usersService.findByAccount(account, true);
        if (user == null) {
            return new ArrayList<>();
        }
        // 1表示借出
        return assetsRepository.findAllByAccountAndStateOrderByAssetName(account, 1);
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

    public AssetsDto findDtoByCode(String code) {
        Assets assets = findByCode(code);
        if (assets == null) {
            return null;
        }
        AssetsDto ret = assets.mapTo();
        Assetclass assetclass = assetclassService.findById(ret.getClassId());
        if (assetclass != null) {
            ret.setClassName(assetclass.getClassName());
        } else {
            ret.setClassName(String.valueOf(ret.getClassId()));
        }
        if (StringUtils.hasText(ret.getAccount())) {
            Users user = usersService.findByAccount(ret.getAccount(), false);
            if (user != null) {
                ret.setUserName(user.getUserName());
            }
        }
        return ret;
    }

    public List<Assets> findByCodeArr(List<String> codeArr) {
        if (codeArr == null || codeArr.isEmpty()) {
            return new ArrayList<>();
        }
        return findByCodeArr(codeArr.toArray(new String[codeArr.size()]));
    }

    public List<Assets> findByCodeArr(String[] codeArr) {
        if (codeArr == null || codeArr.length <= 0) {
            return new ArrayList<>();
        }
        return assetsRepository.findAllByCodeInOrderByClassIdAscAssetNameAsc(codeArr);
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
     * 设置资产借用人，借用人为空表示退库
     *
     * @param code    资产编码
     * @param account 账号
     */
    public void setAssetsUser(String code, String account) {
        Assets assets = findByCode(code);
        if (assets == null) {
            throw new RuntimeException("指定的资产未找到:" + code);
        }
        if (StringUtils.hasText(account)) {
            if (assets.getState() != 0) {
                throw new RuntimeException("该资产不可借:" + code);
            }

            assets.setState(1);
            assets.setAccount(account);
            assets.setAccountTime(LocalDateTime.now());
        } else {
            if (assets.getState() != 1) {
                throw new RuntimeException("该资产不是借出状态:" + code);
            }

            assets.setState(0);
        }
        save(assets);
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

    public void doImport(InputStream inputStream) throws Exception {
        List<List<String>> rows = excelOperator.readExcel(inputStream);
        if (rows == null || rows.size() <= 1) {
            throw new RuntimeException("数据为空，或只有标题行");
        }
        Assets assets = Assets.builder()

                .build();
    }
}
