package com.swg.framework.execution;

import com.swg.framework.api.adb.AdbHelper;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.utility.ExcelUtil;
import com.swg.framework.utility.Util;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Creating reports excel and updating it the results.
 */
public class ExcelResultHelper {
    //  private static List<String> deviceName;

    /**
     * Gets suite file path
     *
     * @return Returns Suite xls path
     * @throws Exception
     */
    public static String getSuiteFile() throws Exception {
        return Util.readFile("Report.Properties", "executionReportPath");
    }

    /**
     * Gets Script list from result sheet
     *
     * @param sheet result
     * @return scriptList
     */
    private static List<String> getScriptList(XSSFSheet sheet) throws Exception {
        List<String> scriptList = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            Cell cell = row.getCell(getDeviceList(sheet).indexOf("ScriptName"));
            scriptList.add(cell.getStringCellValue());
        }
        return scriptList;
    }

    /**
     * -     * Gets TestID list from result sheet
     *
     * @param sheet result
     * @return scriptList
     */
    private static List<String> getTestIDList(XSSFSheet sheet) throws Exception {
        List<String> scriptList = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(getDeviceList(sheet).indexOf("TestCase ID"));
            scriptList.add(cell.getStringCellValue());
            //System.out.println("Setting set value::: "+cell.getStringCellValue());
        }
        return scriptList;
    }

    /**
     * Gets device list from suite
     *
     * @param sheet result
     * @return device list
     */
    private static List<String> getDeviceList(XSSFSheet sheet) throws Exception {
        List<String> deviceList = new ArrayList<>();
        Row header = sheet.getRow(3);
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            deviceList.add(cell.getStringCellValue());
        }
        return deviceList;
    }

    /**
     * Create new result file if not exist and write header data
     *
     * @throws Exception
     */
    public static void setSuiteHeader() throws Exception {
        File excel = new File(getSuiteFile());
        if (!excel.exists()) {
            XSSFWorkbook wb = new XSSFWorkbook();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            excel = new File(excel.getAbsolutePath() + "/Reports/DocAppExecutionReports" + currentDate + ".xlsx");
            String excelData = excel.toString().replace("\\", "/");
            Util.writeInReportPropertiesFile("executionReportPath=" + excelData);
            FileOutputStream fileOutputStream = new FileOutputStream(excel);
            wb.createSheet("TestResults");
            wb.write(fileOutputStream);
            fileOutputStream.close();
        }

        FileInputStream fis = new FileInputStream(excel);
        XSSFWorkbook book = new XSSFWorkbook(fis);
        XSSFSheet sheet = book.getSheet("TestResults");

        //if ((sheet.getRow(0).getCell(0).getStringCellValue().isEmpty()/* == null*/)) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        List<String> deviceName = Arrays.asList(Util.readFile("DeviceInfo.Properties", "devName").split(","));
        String buildVersion = AdbHelper.getAndroidBuildVersion(deviceName);


        CellStyle styleHeader = ExcelUtil.getCellHeaderColorStyle(book);

        Map<String, Object[]> newData = new HashMap<>();
        newData.put("0", new Object[]{"DocApp Execution Report", "", "", ""});
        newData.put("1", new Object[]{"Build No", buildVersion, "", ""});
        newData.put("2", new Object[]{"Date", currentDate, "", ""});
        newData.put("3", new Object[]{"Sr.No.", "Requirement ID", "TestCase ID", "ScriptName"});
        Set<String> newRows = newData.keySet();

        for (String key : newRows) {
            Row row = sheet.createRow(Integer.parseInt(key));
            Object[] objArr = newData.get(key);
            int cellNum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellNum++);
                cell.setCellValue((String) obj);
                if (key.equals("3")) {
                    cell.setCellStyle(styleHeader);
                } else {
                    if (key.equals("0")) {
                        CellStyle style = ExcelUtil.getCellHeaderBorderStyle(book);
                        ExcelUtil.setMergeCellBorderStyle(book, sheet, "A1:I1");
                        style.setAlignment(CellStyle.ALIGN_CENTER);
                        cell.setCellStyle(style);

                    } else {
                        CellStyle style = ExcelUtil.getCellHeaderBorderStyle(book);
                        // ExcelUtil.setMergeCellBorderStyle(book, sheet, "A" + Integer.parseInt(key)+1 + ":" + "I" + Integer.parseInt(key)+1);
                        style.setAlignment(CellStyle.ALIGN_LEFT);
                        cell.setCellStyle(style);
                    }
                }
            }
        }
        //  }
        // open an OutputStream to save written data into Excel file
        FileOutputStream os = new FileOutputStream(excel);
        book.write(os);
        if (!excel.canWrite()) {
            FrameworkLogger.logFail("Excel write operation not performed.");
        }
        // Close workbook, OutputStream and Excel file to prevent leak
        os.flush();
        os.close();
        fis.close();
    }

    /**
     * Write Script name and Device name in test result sheet
     *
     * @param requirement : Test case requirement id
     * @param TestCase    : Test case name
     * @param scriptName  : Script Name
     * @param deviceName  : Device Name
     * @throws Exception
     */
    public static void setDeviceAndScriptName(String requirement, String TestCase, String scriptName, String deviceName) throws Exception {
        File excel = new File(getSuiteFile());
        FileInputStream fis = new FileInputStream(excel);
        XSSFWorkbook book = new XSSFWorkbook(fis);

        XSSFSheet sheet = book.getSheet("TestResults");
        List<String> deviceList = getDeviceList(sheet);
        /* List<String> scriptList = getScriptList(sheet);*/
        List<String> testIDList = getTestIDList(sheet);


        if (!deviceList.contains(deviceName)) {
            Row row = sheet.getRow(3);
            Cell cell = row.createCell(row.getLastCellNum());
            cell.setCellValue(deviceName);
            cell.setCellStyle(ExcelUtil.getCellHeaderColorStyle(book));
        }
        if (!testIDList.contains(TestCase)) {

            Map<String, Object[]> newData = new HashMap<>();
            newData.put("1", new Object[]{String.valueOf((sheet.getLastRowNum() + 1) - 3), requirement, TestCase, scriptName});
            Set<String> newRows = newData.keySet();
            for (String key : newRows) {
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                Object[] objArr = newData.get(key);
                int cellNum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellNum++);
                    if (obj instanceof String) {
                        cell.setCellValue((String) obj);
                        cell.setCellStyle(ExcelUtil.getCellBorderStyle(book));
                    } else if (obj instanceof Boolean) {
                        cell.setCellValue((Boolean) obj);
                    } else if (obj instanceof Date) {
                        cell.setCellValue((Date) obj);
                    } else if (obj instanceof Double) {
                        cell.setCellValue((Double) obj);
                    }
                }
            }
        }
        // open an OutputStream to save written data into Excel file
        FileOutputStream os = new FileOutputStream(excel);
        book.write(os);
        // Close workbook, OutputStream and Excel file to prevent leak
        os.close();
        fis.close();
    }

    /**
     * Write test results in result sheet
     *
     * @param status     : Script status
     * @param testID     : TestCase ID
     * @param deviceName : Device Name
     * @throws Exception : Exception
     */
    private static void setTestStatus(String status, String testID, String deviceName, String filename) throws Exception {
        status = status.trim();
        File excel = new File(getSuiteFile());
        FileInputStream fis = new FileInputStream(excel);
        XSSFWorkbook book = new XSSFWorkbook(fis);

        XSSFSheet sheet = book.getSheet("TestResults");
        List<String> deviceList = getDeviceList(sheet);

        //List<String> scriptList = getScriptList(sheet);
        List<String> testIDList = getTestIDList(sheet);

        Row row = sheet.getRow(testIDList.indexOf(testID));
        Cell cell = row.createCell(deviceList.indexOf(deviceName));
        cell.setCellValue(status.trim());
        System.out.println("Execution Status:::: "+ status);
        cell.setCellStyle(ExcelUtil.getResultStyle(book, status));
        XSSFCreationHelper helper = book.getCreationHelper();
        XSSFHyperlink file_link = helper.createHyperlink(Hyperlink.LINK_FILE);
        file_link.setAddress("file:." + Util.readFile(filename, "logFilePath"));
        //file_link.setTooltip("Click to open the file");
        cell.setHyperlink(file_link);
        // open an OutputStream to save written data into Excel file
        FileOutputStream os = new FileOutputStream(excel);
        book.write(os);
        // Close workbook, OutputStream and Excel file to prevent leak
        os.close();

        fis.close();
    }


    /**
     * Write Summary sheet data
     *
     * @throws Exception : Exception
     */
    private static void setSummaryTemplate() throws Exception {
        File excel = new File(getSuiteFile());
        List<String> featureList = new ArrayList<>();
        Collections.addAll(featureList, "Functionality", "International Number", "Landing Page", "Profile",
                "SyncAccount", "MedicalHistory", "Offline Mode", "Misc", "OTP-Login", "My Health Data",
                "Search & Filter", "Sharing & Notification", "DICOM", "Health Charts", "Total");

        // List<String> osVersion = Arrays.asList(Util.readFile("DeviceInfo.Properties", "osVersion").split(","));
        String[] deviceColumnName = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
        FileInputStream fis = new FileInputStream(excel);
        XSSFWorkbook book = new XSSFWorkbook(fis);
        XSSFSheet sheet = book.getSheet("TestResults");
        List<String> deviceList = getDeviceList(sheet);
        sheet = book.getSheet("Result Summary");
        Row row = sheet.getRow(10);
        Cell cell = row.getCell(0);
        if (cell.getStringCellValue().isEmpty()) {
            System.out.println("Summary Sheet Updated");
            //  if (deviceList.contains(deviceName)) {
            int index = 6;
            for (int i = 0; i < deviceList.size(); i++) {
                if (i > 3) {
                    row = sheet.getRow(1);
                    cell = row.createCell(1);
                    String strFormula = "TestResults!A1";
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(strFormula);
                    String range = deviceColumnName[cell.getColumnIndex()] + cell.getRowIndex() + ":" + deviceColumnName[deviceColumnName.length - 1] + cell.getRowIndex();
                    ExcelUtil.setMergeCellBorderStyle(book, sheet, range);
                    // cell.setCellStyle(ExcelUtil.getCellBorderStyle(book));

                    row = sheet.getRow(2);
                    cell = row.createCell(1);
                    strFormula = "TestResults!B2";
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(strFormula);
                    range = deviceColumnName[cell.getColumnIndex()] + cell.getRowIndex() + ":" + deviceColumnName[deviceColumnName.length - 1] + cell.getRowIndex();
                    ExcelUtil.setMergeCellBorderStyle(book, sheet, range);
                    // cell.setCellStyle(ExcelUtil.getCellBorderStyle(book));

                    row = sheet.getRow(3);
                    cell = row.createCell(1);
                    strFormula = "TestResults!B3";
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(strFormula);
                    range = deviceColumnName[cell.getColumnIndex()] + cell.getRowIndex() + ":" + deviceColumnName[deviceColumnName.length - 1] + cell.getRowIndex();
                    ExcelUtil.setMergeCellBorderStyle(book, sheet, range);
                    //cell.setCellStyle(ExcelUtil.getCellBorderStyle(book));

                    //Setting Feature Name
                    setFeatureList(sheet, book, featureList);
                    setDeviceAndOS(sheet, book, featureList, deviceColumnName);
                    index++;
                }
            }
            // open an OutputStream to save written data into Excel file
            FileOutputStream os = new FileOutputStream(excel);
            book.write(os);
            // Close workbook, OutputStream and Excel file to prevent leak
            os.close();
            fis.close();
        }
    }

    private static void setFeatureList(XSSFSheet sheet, XSSFWorkbook book, List<String> featureList) {
        int index = 8;
        for (int i = 0; i < featureList.size(); i++) {
            Row row;
            Cell cell;
            if (i == 0) {
                row = sheet.createRow(index - 2);
                cell = row.createCell(0);
                cell.setCellValue(featureList.get(i));
                cell.setCellStyle(ExcelUtil.getReportHeaderBorderStyle(book));
            } else {
                row = sheet.createRow(index + i);
                cell = row.createCell(0);
                cell.setCellValue(featureList.get(i));
                if (featureList.get(i).equals("Total")) {
                    Font font = book.createFont();
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    CellStyle style = ExcelUtil.getCellBorderStyle(book);
                    style.setFont(font);
                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                    cell.setCellStyle(style);

                } else {
                    cell.setCellStyle(ExcelUtil.getCellBorderStyle(book));
                }

            }

        }
    }

    private static void setDeviceAndOS(XSSFSheet sheet, XSSFWorkbook book, List<String> featureList, String[] deviceColumnName) throws Exception {
        List<String> deviceList = Arrays.asList(Util.readFile("DeviceInfo.Properties", "deviceModel").replaceAll(" ", "_").split(","));
        List<String> osVersionList = Arrays.asList(Util.readFile("DeviceInfo.Properties", "osVersion").split(","));
        int rowIndex = 6;
        int cellIndex = 0;
        Row row;
        Cell cell;
        for (int i = 0; i < deviceList.size(); i++) {
            //Set DeviceName
            row = sheet.getRow(rowIndex);
            cell = row.createCell(cellIndex + 1);
            cell.setCellValue(deviceList.get(i));

            String range = deviceColumnName[cell.getColumnIndex()] + cell.getRowIndex() + ":" + deviceColumnName[cell.getColumnIndex() + 1] + cell.getRowIndex();
            ExcelUtil.setMergeCellBorderStyle(book, sheet, range);
            cell.setCellStyle(ExcelUtil.getReportHeaderBorderStyle(book));


            //Set osVersion
            row = sheet.getRow(rowIndex + 1);
            cell = row.createCell(cellIndex + 1);
            cell.setCellValue(osVersionList.get(i));

            range = deviceColumnName[cell.getColumnIndex()] + cell.getRowIndex() + ":" + deviceColumnName[cell.getColumnIndex() + 1] + cell.getRowIndex();
            ExcelUtil.setMergeCellBorderStyle(book, sheet, range);
            cell.setCellStyle(ExcelUtil.getReportHeaderBorderStyle(book));


            //Set Percentage
            int rowPercentageIndex = rowIndex + 2 + featureList.size();
            row = sheet.getRow(rowPercentageIndex);
            cell = row.createCell(cellIndex);
            cell.setCellValue("% TestCase Passed");
            cell.setCellStyle(ExcelUtil.getReportHeaderBorderStyle(book));
            cell = row.createCell(cellIndex + 1);
            String strFormula = "ROUND(" + deviceColumnName[cellIndex + 1] + rowPercentageIndex +
                    "/(" + deviceColumnName[cellIndex + 1] + rowPercentageIndex + "+" + deviceColumnName[cellIndex + 2] +
                    rowPercentageIndex + ")*100,0)";
            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            cell.setCellFormula(strFormula);

            range = deviceColumnName[cell.getColumnIndex()] + cell.getRowIndex() + ":" + deviceColumnName[cell.getColumnIndex() + 1] + cell.getRowIndex();
            ExcelUtil.setMergeCellBorderStyle(book, sheet, range);
            cell.setCellStyle(ExcelUtil.getReportHeaderBorderStyle(book));

            //Set Pass/Fail Label
            row = sheet.getRow(rowIndex + 2);
            cell = row.createCell(cellIndex + 1);
            cell.setCellValue("#Pass");
            cell.setCellStyle(ExcelUtil.getReportHeaderBorderStyle(book));

            row = sheet.getRow(rowIndex + 2);
            cell = row.createCell(cellIndex + 2);
            cell.setCellValue("#Fail");
            cell.setCellStyle(ExcelUtil.getReportHeaderBorderStyle(book));

            Collections.addAll(featureList, "Functionality", "International Number", "Landing Page", "Profile",
                    "SyncAccount", "MedicalHistory", "Offline Mode", "Misc", "OTP-Login", "My Health Data",
                    "Search & Filter", "Sharing & Notification", "DICOM", "Health Charts", "Total");
            //Set Pass/Fail status
            int statusIndex = 9;
            String[] featureCode = {"", "INT", "LP", "PRF", "SYN", "MED", "OFF", "MCS", "OTP", "MHD", "S&F", "SHR",
                    "HH_DIC", "HCH"};
            for (int j = 1; j < featureList.size(); j++) {
                if (j != featureList.size() - 1) {
                    row = sheet.getRow(statusIndex);
                    cell = row.createCell(cellIndex + 1);
                    String colName = deviceColumnName[i + 4];
                    strFormula = "COUNTIFS(TestResults!$C:$C,\"" + featureCode[j] + "*\",TestResults!$" + colName + ":$" + colName + ",\"Pass\")";
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(strFormula);
                    Font font = book.createFont();
                    font.setColor(IndexedColors.GREEN.getIndex());
                    CellStyle style = ExcelUtil.getResultCellBorderStyle(book);
                    style.setFont(font);
                    cell.setCellStyle(style);

                    row = sheet.getRow(statusIndex);
                    cell = row.createCell(cellIndex + 2);
                    colName = deviceColumnName[i + 4];
                    strFormula = "COUNTIFS(TestResults!$C:$C,\"" + featureCode[j] + "*\",TestResults!$" + colName + ":$" + colName + ",\"Fail\")";
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(strFormula);
                    font = book.createFont();
                    font.setColor(IndexedColors.RED.getIndex());
                    style = ExcelUtil.getResultCellBorderStyle(book);
                    style.setFont(font);
                    cell.setCellStyle(style);
                } else {
                    row = sheet.getRow(statusIndex);
                    cell = row.createCell(cellIndex + 1);
                    strFormula = "SUM(" + deviceColumnName[cell.getColumnIndex()] + "10:" + deviceColumnName[cell.getColumnIndex()] + "22)";
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(strFormula);
                    Font font = book.createFont();
                    font.setColor(IndexedColors.GREEN.getIndex());
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    CellStyle style = ExcelUtil.getResultCellBorderStyle(book);
                    style.setFont(font);
                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                    cell.setCellStyle(style);

                    row = sheet.getRow(statusIndex);
                    cell = row.createCell(cellIndex + 2);
                    strFormula = "SUM(" + deviceColumnName[cell.getColumnIndex()] + "10:" + deviceColumnName[cell.getColumnIndex()] + "22)";
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(strFormula);
                    font = book.createFont();
                    font.setColor(IndexedColors.RED.getIndex());
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    style = ExcelUtil.getResultCellBorderStyle(book);
                    style.setFont(font);
                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                    cell.setCellStyle(style);
                }
                statusIndex++;
            }

            cellIndex = cellIndex + 2;
        }

    }

    public static void updateTestCasesResultInExcel() throws Exception {

//        List<String> deviceList = Arrays.asList(Util.readFile("DeviceInfo.Properties", "deviceModel").replaceAll(" ", "_").split(","));
//        String testName = Util.readFile(deviceList.get(0) + ".Properties", "testName");
//        int deviceCount = Integer.parseInt(Util.readFile("DeviceInfo.Properties", "numberOfDevices"));
//        for (int i = 1; i <= deviceCount; i++) {
//            String device = Util.readFile("DeviceInfo.Properties", "DeviceName" + i).replaceAll(" ", "_");
//            String testStatus = Util.readFile(deviceList.get(i - 1) + ".Properties", device);
//            setTestStatus(testStatus, testName, device, deviceList.get(i - 1) + ".Properties");
//        }

        List<String> deviceList = Arrays.asList(Util.readFile("DeviceInfo.Properties", "deviceModel").
                replaceAll(" ", "_").split(","));
        for (String eachDevice : deviceList) {
            Map<String, String> testResults = Util.getPropertyMap(eachDevice + ".Properties");
            List<String> keys = new ArrayList<>();
            keys.addAll(testResults.keySet());

            for (String key : keys) {
                if (!key.equals("testName") && !key.equals("logFilePath") && !key.equals("executionTime")) {
                    setTestStatus(testResults.get(key), key, eachDevice, eachDevice + ".Properties");
                }
            }
        }
    }


    public static void main(String args[]) throws Exception {
        updateTestCasesResultInExcel();
        setSummaryTemplate();
    }

}
