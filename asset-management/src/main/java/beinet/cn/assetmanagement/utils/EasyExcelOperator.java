package beinet.cn.assetmanagement.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class EasyExcelOperator implements ExcelOperator {
    @Override
    public List<List<String>> readExcel(InputStream stream) {
        Sheet sheet = new Sheet(1, 0);
        List<Object> readList = EasyExcelFactory.read(stream, sheet);
        List<List<String>> ret = new ArrayList<>();
        for (Object obj : readList) {
            ret.add((List<String>) obj);
        }
        return ret;
    }

    @Override
    public void writeExcel(OutputStream stream, List<List<String>> data) throws Exception {
        Sheet sheet = new Sheet(1, 0);
        sheet.setSheetName("data");
        sheet.setAutoWidth(true);

        ExcelWriter writer = new ExcelWriter(stream, ExcelTypeEnum.XLSX);
        writer.write0(data, sheet);
        writer.finish();
    }

    public void writeExcelModel(OutputStream stream, List<? extends BaseRowModel> data) throws Exception {
        Sheet sheet = new Sheet(1, 0);
        sheet.setSheetName("data");
        sheet.setAutoWidth(true);

        ExcelWriter writer = new ExcelWriter(stream, ExcelTypeEnum.XLSX);
        writer.write(data, sheet);
        writer.finish();
    }
}
