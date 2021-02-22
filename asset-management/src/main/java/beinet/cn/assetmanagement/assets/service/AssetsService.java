package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assetclass;
import beinet.cn.assetmanagement.assets.model.Assets;
import beinet.cn.assetmanagement.assets.model.AssetsDto;
import beinet.cn.assetmanagement.assets.repository.AssetsRepository;
import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.service.UsersService;
import beinet.cn.assetmanagement.utils.DateTimeHelper;
import beinet.cn.assetmanagement.utils.ExcelOperator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssetsService {
    private final AssetsRepository assetsRepository;
    private final AssetclassService assetclassService;
    private final UsersService usersService;

    private final ExcelOperator excelOperator;

    private static final Map<String, String> templateMap = new HashMap<>();
    private static final Map<String, Integer> assetStateMap = new HashMap<>();

    static {
        // 导入模板的标题，与Entity字段映射关系
        templateMap.put("资产分类", "classId");
        templateMap.put("资产名称", "assetName");
        templateMap.put("资产说明", "description");
        templateMap.put("购买时间", "buyTime");
        templateMap.put("购买价格", "price");
        templateMap.put("库房位置", "place");
        templateMap.put("资产状态", "state");
        templateMap.put("最后借用人", "account");
        templateMap.put("最后借用时间", "accountTime");

        // 状态模板
        assetStateMap.put("库存", 0);
        assetStateMap.put("借出", 1);
        assetStateMap.put("故障", 2);
        assetStateMap.put("报废", 3);
    }

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

    public List<Assets> findByUser(String account) {
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
        return findByCodeArr(codeArr.toArray(new String[0]));
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
     * @return 序列号
     */
    public String getCode(Assets item) {
        return item.getBuyTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                String.format("%04d", item.getClassId()) +
                String.format("%06d", assetclassService.updateAndGetAmount(item.getClassId()));
    }


    public List<List<String>> getExcelData(String account) {
        List<Assets> result = findAll(null, account);
        List<List<String>> excelData = new ArrayList<>();

        List<String> titleRow = new ArrayList<>();
        excelData.add(titleRow);
        // 添加标题
        titleRow.add("资产分类");
        titleRow.add("资产名称");
        titleRow.add("资产说明");
        titleRow.add("购买时间");
        titleRow.add("购买价格");
        titleRow.add("库房位置");
        titleRow.add("资产状态");
        titleRow.add("最后借用人");
        titleRow.add("最后借用时间");
        titleRow.add("入库时间");
        titleRow.add("资产编号");

        for (Assets assets : result) {
            List<String> row = new ArrayList<>();
            excelData.add(row);

            row.add(getClassName(assets.getClassId()));
            row.add(assets.getAssetName());
            row.add(assets.getDescription());
            row.add(assets.getBuyTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            row.add(String.valueOf(assets.getPrice() / 100d));
            row.add(assets.getPlace());
            row.add(getStateName(assets.getState()));
            row.add(getNameByAccount(assets.getAccount()));
            row.add(assets.getAccountTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            row.add(assets.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            row.add(assets.getCode());
        }
        return excelData;
    }

    @Transactional
    public List<String> doImport(InputStream inputStream) throws Exception {
        List<List<String>> rows = excelOperator.readExcel(inputStream);
        if (rows == null || rows.size() <= 1) {
            throw new RuntimeException("数据为空，或只有标题行");
        }

        List<String> result = new ArrayList<>();

        Map<String, Integer> colMap = null;
        int idx = 0;
        int successNum = 0;
        for (List<String> row : rows) {
            idx++;
            if (idx == 1 || colMap == null) {
                // 第一行是标题，要用于映射
                colMap = getColIndexMap(row);
                continue;
            }
            String assetName = getItemValue(row, colMap, "assetName");
            String className = getItemValue(row, colMap, "classId");
            if (assetName.length() <= 0 && className.length() <= 0) {
                continue;
            }

            // 资产名必填
            if (assetName.length() <= 0) {
                result.add("第" + idx + "行：资产名称必填");
                continue;
            }
            int classId = getClassId(className);
            if (classId <= 0) {
                result.add("第" + idx + "行：资产分类不存在:" + className);
                continue;
            }
            String stateName = getItemValue(row, colMap, "state");
            int state = getState(stateName);
            if (state < 0) {
                result.add("第" + idx + "行：状态设置有误:" + stateName);
                continue;
            }
            String userName = getItemValue(row, colMap, "account");
            String account = "";
            if (userName.length() > 0) {
                account = getAccountByName(userName);
                if (StringUtils.isEmpty(account)) {
                    result.add("第" + idx + "行：借用人姓名找不到对应的账号:" + userName);
                    continue;
                }
            }

            Assets assets = Assets.builder()
                    .classId(classId)
                    .assetName(assetName)
                    .description(getItemValue(row, colMap, "description"))
                    .buyTime(convertToTime(getItemValue(row, colMap, "buyTime")))
                    .price(convertToInt(getItemValue(row, colMap, "price")))
                    .place(getItemValue(row, colMap, "place"))
                    .state(state)
                    .account(account)
                    .accountTime(convertToTime(getItemValue(row, colMap, "accountTime")))
                    .build();
            try {
                save(assets);
                successNum++;
            } catch (Exception exp) {
                result.add("第" + idx + "行：添加错误:" + exp.getMessage());
            }
        }
        result.add(0, "成功导入条数：" + successNum);
        return result;
    }

    // 获取字段与Excel的列的映射关系
    private Map<String, Integer> getColIndexMap(List<String> titles) {
        Map<String, Integer> ret = new HashMap<>();
        for (int i = 0, j = titles.size(); i < j; i++) {
            String title = titles.get(i);
            // 存在重复列时，以第一次出现的列为准
            if (ret.containsKey(title)) {
                continue;
            }
            String colName = templateMap.get(title);
            ret.put(colName, i);
        }
        return ret;
    }

    private String getItemValue(List<String> rowValues, Map<String, Integer> colMap, String colName) {
        Integer idx = colMap.get(colName);
        if (idx == null || idx < 0 || idx >= rowValues.size()) {
            return "";
        }
        String ret = rowValues.get(idx);
        if (ret == null) {
            return "";
        }
        return ret.trim();
    }

    private String getAccountByName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return "";
        }
        Users user = usersService.findByUserName(userName, false);
        return user == null ? "" : user.getAccount();
    }

    private String getNameByAccount(String account) {
        if (StringUtils.isEmpty(account)) {
            return "";
        }
        Users user = usersService.findByAccount(account, false);
        return user == null ? "" : user.getUserName();
    }

    private int getClassId(String val) {
        if (StringUtils.isEmpty(val)) {
            return 0;
        }
        Assetclass assetclass = assetclassService.findByName(val);
        return assetclass == null ? 0 : assetclass.getId();
    }

    private String getClassName(int classId) {
        if (classId <= 0) {
            return String.valueOf(classId);
        }
        Assetclass assetclass = assetclassService.findById(classId);
        return assetclass == null ? String.valueOf(classId) : assetclass.getClassName();
    }

    private LocalDateTime convertToTime(String val) {
        if (val.length() <= 0) {
            return DateTimeHelper.MIN;
        }
        String dateStr = DateTimeHelper.formatExcelDate(Double.parseDouble(val));
        return DateTimeHelper.toDateTime(dateStr);
    }

    private int convertToInt(String val) {
        if (StringUtils.isEmpty(val)) {
            return 0;
        }
        return (int) Double.parseDouble(val) * 100;
    }

    private int getState(String val) {
        Integer ret = assetStateMap.get(val);
        if (ret == null) {
            return -1;
        }
        return ret;
    }

    private String getStateName(int val) {
        for (Map.Entry<String, Integer> entry : assetStateMap.entrySet()) {
            if (entry.getValue().equals(val)) {
                return entry.getKey();
            }
        }
        return String.valueOf(val);
    }
}
