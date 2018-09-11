package com.hackathon.prophet.utils;

import com.hackathon.prophet.constant.FileConstants;
import com.hackathon.prophet.pojo.SingleDtsBase;
import jxl.Cell;
import jxl.CellView;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;
import org.xml.sax.SAXException;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

public class ExcelUtils {

    public static <T> List<T> parse(File file, Class<T> clazz, String xml) {
        InputStream configIs = ExcelUtils.class.getResourceAsStream(xml);
        InputStream excelIs = null;
        try {
            excelIs = new FileInputStream(file);
        }
        catch (FileNotFoundException e){

        }
        return parseExcelFileToBean(excelIs, configIs);
    }

    public static boolean isFileType(String contentType) {
        if (!contentType.contains(FileConstants.XLS_TYPE) && !contentType.contains(FileConstants.XLSX_TYPE)) {
            return false;
        }
        return true;
    }

    private static <T> List<T> parseExcelFileToBean(InputStream xlsFilsInputStream, InputStream jxlsConfigInputStream)
    {
        List<T> result = new ArrayList<T>();
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("result", result);
        InputStream is = null;
        try {
            XLSReader xlsReader = ReaderBuilder.buildFromXML(jxlsConfigInputStream);
            is= new BufferedInputStream(xlsFilsInputStream);
            xlsReader.read(is, beans);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(xlsFilsInputStream);
            IOUtils.closeQuietly(jxlsConfigInputStream);
        }
        return result;
    }

    public static void writeExcel(List<SingleDtsBase> records, File file) throws IOException, WriteException, RowsExceededException {
        WritableWorkbook workbook = null;
        try
        {
            workbook = Workbook.createWorkbook(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        WritableSheet sheet = workbook.createSheet("sheetName", 0);
        // add a blank row
        for (SingleDtsBase record : records)
        {
            Label label1 = new Label(0,0,"id");
            sheet.addCell(label1);
        }
        sheet.insertRow(sheet.getRows());
        // create label from object, then insert
        workbook.write();
        workbook.close();
    }

}
