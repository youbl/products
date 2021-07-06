package com.chaoip.ipproxy.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class CityHelper {
    private static final String cityFile = "/data/app/area.txt";
    private static Map<String, String[]> citys = loadFile(cityFile);

    private CityHelper() {
    }


    public static Map<String, String[]> getCitys() {
        return citys;
    }

    /**
     * 根据区号找城市名
     *
     * @param code 区号
     * @return 城市或未知
     */
    public static String getByAreaCode(String code) {
        String[] city = getArrByAreaCode(code);
        return city == null ? "" : city[0];
    }

    /**
     * 根据区号找省市名
     *
     * @param code 区号
     * @return 0为城市 1为省份
     */
    public static String[] getArrByAreaCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return citys.getOrDefault(code, null);
    }

    /**
     * 重新加载城市文件
     */
    public static void reloadCitys() {
        citys = loadFile(cityFile);
    }

    private static Map<String, String[]> loadFile(String file) {
        File objFile = new File(file);
        if (!objFile.isFile())
            return new HashMap<>();

        Map<String, String[]> ret = new HashMap<>();
        //        citys.put("0311", new String[]{"河北省石家庄", "河北"});
        try (BufferedReader br = new BufferedReader(new FileReader(objFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 山西省	朔州市	0349
                String[] arr = line.split("\t");
                if (arr.length < 3)
                    continue;
                String cityName = arr[1];
                String province = arr[0];
                if (!province.equals(cityName)) {
                    cityName = province + cityName;
                }
                ret.put(arr[2], new String[]{cityName, province});
            }
        } catch (Exception e) {
            log.error("读城市出错", e);
        }

        return ret;
    }
}
