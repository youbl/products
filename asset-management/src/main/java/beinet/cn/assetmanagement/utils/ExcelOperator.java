package beinet.cn.assetmanagement.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ExcelOperator {
    /**
     * 读取Excel文件，返回数组的数组, 包含标题行
     * 每行是一个List String
     *
     * @param fileName 文件
     * @return Excel内容
     * @throws Exception 异常
     */
    default List<List<String>> readExcel(String fileName) throws Exception {
        return readExcel(new FileInputStream(fileName));
    }

    /**
     * 读取输入流，返回数组的数组, 包含标题行
     * 每行是一个List String
     *
     * @param stream 输入流
     * @return Excel内容
     * @throws Exception 异常
     */
    List<List<String>> readExcel(InputStream stream) throws Exception;

    /**
     * 写入Excel文件
     *
     * @param fileName 文件
     * @param data     数据，每行是一个List String，包含标题行
     * @throws Exception 异常
     */
    default void writeExcel(String fileName, List<List<String>> data) throws Exception {
        writeExcel(new FileOutputStream(fileName), data);
    }

    /**
     * 写入Excel文件流
     * 每行是一个List<String></String>
     *
     * @param stream 输出流
     * @param data   数据，每行是一个List String，包含标题行
     * @throws Exception 异常
     */
    void writeExcel(OutputStream stream, List<List<String>> data) throws Exception;
}
