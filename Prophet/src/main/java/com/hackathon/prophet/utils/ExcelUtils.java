package com.hackathon.prophet.utils;

import com.hackathon.prophet.constant.FileConstants;
import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    private static final String CONF = "/excelMapper.xml";

    public static <T> List<T> parse(File file, Class<T> clazz) {
        InputStream configIs = ExcelUtils.class.getResourceAsStream(CONF);
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

}
