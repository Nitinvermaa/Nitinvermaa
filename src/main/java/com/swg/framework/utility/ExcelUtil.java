package com.swg.framework.utility;

import com.swg.framework.common.Constants;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

/**
 * Helper class to get excel file and set styles and borders.
 */
public class ExcelUtil {
    static String rootPath = System.getProperty("user.dir");

    /**
     * Set Cell color
     *
     * @param book
     * @return
     */
    public static CellStyle getCellBorderStyle(XSSFWorkbook book) {
        CellStyle style = book.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        return style;
    }

    /**
     * Set Cell color
     *
     * @param book
     * @return
     */
    public static void setMergeCellBorderStyle(XSSFWorkbook book, XSSFSheet sheet, String cellRange) {
        CellRangeAddress RangeAddress = CellRangeAddress.valueOf(cellRange);
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, RangeAddress, sheet, book);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, RangeAddress, sheet, book);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, RangeAddress, sheet, book);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, RangeAddress, sheet, book);
    }


    /**
     * Set Cell color
     *
     * @param book
     * @return
     */
    public static CellStyle getResultCellBorderStyle(XSSFWorkbook book) {
        CellStyle style = book.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        return style;
    }

    /**
     * Set Cell Border
     *
     * @param book
     * @return
     */
    public static CellStyle getCellHeaderBorderStyle(XSSFWorkbook book) {
        CellStyle style = book.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        return style;
    }

    /**
     * Set Cell Color
     *
     * @param book
     * @return
     */
    public static CellStyle getCellHeaderColorStyle(XSSFWorkbook book) {
        CellStyle style = book.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        return style;
    }

    /**
     * Set Cell Color
     *
     * @param book
     * @return
     */
    public static CellStyle getResultStyle(XSSFWorkbook book, String status) {
        CellStyle style = book.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        Font font = book.createFont();
        if (status.equals("Pass")) {
            font.setColor(IndexedColors.GREEN.getIndex());
        } else {
            font.setColor(IndexedColors.RED.getIndex());
        }
        style.setFont(font);
        return style;
    }

    /**
     * Gets sheet form the xls
     *
     * @return sheet
     * @throws Exception
     */
    public static XSSFSheet getSheet(String suiteFile) throws Exception {
        File myFile = new File(suiteFile);
        FileInputStream fis = new FileInputStream(myFile);
        XSSFWorkbook book = new XSSFWorkbook(fis);
        return book.getSheetAt(0);
    }

    /**
     * Gets suite file path
     *
     * @return Returns Suite xls path
     * @throws Exception
     */
    public static String getTestSuite() throws Exception {
        return rootPath + Util.readProperties(Constants.MOBILE_EXCEL_FILE);
    }

    /**
     * Set Cell Border
     *
     * @param book : excel sheet
     * @return : style
     */
    public static CellStyle getReportHeaderBorderStyle(XSSFWorkbook book) {
        CellStyle style = book.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        return style;
    }


}
