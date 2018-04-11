package com.poi.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.apache.poi.hssf.util.HSSFColor.*;

/**
 * 利用开源组件POI3.13动态导出excel文档
 *
 * @author Zheng
 */
public class ExcelUtils {
    /**
     * 这是一个通用方法
     *
     * @param fileName 文件名
     * @param rowsName 表格属性列名数组
     * @param dataList 需要显示的数据集合
     * @param pattern  如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     * @throws IOException 读写异常
     */
    @SuppressWarnings("deprecation")
    public static void exportExcel(String fileName,
                                   String[] rowsName,
                                   List<Object[]> dataList,
                                   String pattern,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        // 文件夹目录
        String docPath = request.getServletContext().getRealPath("/doc/");
        // 文件存储绝对路径
        String filePath = docPath + File.separator + fileName + ".xls";
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个工作表
        HSSFSheet sheet = workbook.createSheet("Excel");
        // 设置表格默认列宽度为15个字节
        //sheet.setDefaultColumnWidth((short) 15);
        // 生成一个表格标题行样式
        HSSFCellStyle thStyle = getColumnTopStyle(workbook);
        // 生成非标题样式
        HSSFCellStyle tdStyle = getColumnStyle(workbook);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < rowsName.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(thStyle);
            cell.setCellValue(rowsName[i]);
        }

        // 遍历集合数据，产生数据行
        Iterator<Object[]> it = dataList.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);//从第1行开始创建
            Object[] obj = it.next();
            for (short i = 0; i < obj.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(tdStyle);
                Object value = obj[i];
                String textValue = "";
                if (!"".equals(value) && value != null) {
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        cell.setCellValue(intValue);
                    } else if (value instanceof Float) {
                        float fValue = (Float) value;
                        cell.setCellValue(fValue);
                    } else if (value instanceof Double) {
                        double dValue = (Double) value;
                        cell.setCellValue(dValue);
                    } else if (value instanceof Long) {
                        long longValue = (Long) value;
                        cell.setCellValue(longValue);
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        if (null == pattern || pattern.equals("")) {
                            pattern = "yyyy-MM-dd";
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                        cell.setCellValue(textValue);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                        cell.setCellValue(textValue); // 设置单元格的值
                    }
                } else {
                    cell.setCellValue("");
                }
            }
        }

        // 创建表格之后设置行高与列宽
        for (int rowNum = 0; rowNum < dataList.size() + 1; rowNum++) {// 要加上td
            row = sheet.getRow(rowNum);
            row.setHeightInPoints(30);
        }
        for (int colNum = 0; colNum < rowsName.length; colNum++) {
            sheet.setColumnWidth(colNum, rowsName[colNum].getBytes().length * 2 * 256);
        }

        // 创建excel流程
        if (createDir(docPath)) {
            // 生成excel表格
            OutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            out.close();
            // 执行下载
            download(filePath, response);
            // 删除已完成下载的文件
            new File(filePath).delete();
        }
    }

    /**
     * 列头单元格样式
     *
     * @param workbook 工作簿
     * @return 单元格样式
     */
    private static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColorPredefined.BLACK.getIndex());
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体颜色
        font.setColor(HSSFColorPredefined.VIOLET.getIndex());
        // 设置字体大小
        font.setFontHeightInPoints((short) 12);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 在样式用应用设置的字体;
        style.setFont(font);

        return style;
    }

    /**
     * 列数据信息单元格样式
     *
     * @param workbook 工作簿
     * @return 单元格样式
     */
    private static HSSFCellStyle getColumnStyle(HSSFWorkbook workbook) {
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColorPredefined.BLACK.getIndex());
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);
        // 设置字体名字
        font.setFontName("Courier New");
        // 在样式用应用设置的字体;
        style.setFont(font);

        return style;
    }

    /**
     * 创建目录
     *
     * @param dirName 目录路径
     * @return true 存在目录  false 不存在目录
     */
    private static boolean createDir(String dirName) {
        File dir = new File(dirName);
        // 判断目录是否存在
        if (dir.exists()) {
            return true;
        }
        // 判断目录路径后是否有 /
        if (!dirName.endsWith(File.separator)) {
            dirName = dirName + File.separator;
        }
        // 创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + dirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + dirName + "失败！");
            return false;
        }
    }

    /**
     * 导出下载,弹出下载框
     *
     * @param filePath 文件下载路径
     * @param response 响应对象
     */
    private static void download(String filePath, HttpServletResponse response) throws IOException {
        // 获得要下载的文件
        File file = new File(filePath);
        // 取得文件名
        String filename = file.getName();

        // 清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.addHeader("Content-Length", "" + file.length());
        response.setContentType("application/vnd.ms-excel;charset=gb2312");

        // 以流的形式下载文件
        // 1.封装数据源
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        // 2.封装目的地
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        // 3.开始读写
        byte[] bytes = new byte[in.available()]; // 可无阻塞读取的字符数量
        int len = 0;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);// 从bytes中读写0-len的字节
            out.flush();
        }
        // 4.关闭流
        out.close();
        in.close();
    }

    /**
     * 清除文件
     *
     * @param filePath 文件路径
     */
    private static void cleanFile(String filePath) {
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println(filePath + "删除成功！");
        } else {
            System.out.println(filePath + "删除失败！");
        }
    }
}
