package com.swg.framework.execution;

import com.swg.framework.utility.ExcelUtil;
import com.swg.framework.utility.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Creating excel file and inserting/updating data
 */
public class HeaderToExcel {
    private static Map<String, Object[]> data = new HashMap<String, Object[]>();
    private static List<String> key = new ArrayList<String>();
    private static List<String> dataKey = new ArrayList<String>();

    /*public static void main(String[] args) throws Exception {
        createNewExcelFile();
        updateSuiteXLS();
    }*/


    /**
     * Create suite file if not exist
     *
     * @throws Exception
     */
    public static void createNewExcelFile() throws Exception {
        File myFile = new File(ExcelUtil.getTestSuite());
        if (!myFile.exists()) {
            //Blank workbook
            XSSFWorkbook wb = new XSSFWorkbook();
            FileOutputStream fileOut = new FileOutputStream(myFile);
            //Create a blank sheet
            XSSFSheet sheet = wb.createSheet("SuiteFile");
            //This data needs to be written (Object[])
            Map<String, Object[]> data = new TreeMap<String, Object[]>();

            data.put("1", new Object[]{"Feature Group", "Test Script", "Test Script Description", "Test Case ID", "Requirement ID", "IOS", "", "Android", "", "Final Execution", "Suite Name", "Suite to execute"});
            data.put("2", new Object[]{"", "", "", "", "", "Applicable", "Execution", "Applicable", "Execution", "", "", "Feature to Execute"});
            CellStyle borderCellStyle = ExcelUtil.getCellHeaderBorderStyle(wb);
            //Iterate over data and write to sheet
            Set<String> keySet = data.keySet();
            int rowNum = 0;
            for (String key : keySet) {
                Row row = sheet.createRow(rowNum++);
                Object[] objArr = data.get(key);
                int cellNum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellNum++);
                    if (obj instanceof String) {
                        cell.setCellValue((String) obj);
                        cell.setCellStyle(borderCellStyle);
                    } else if (obj instanceof Integer)
                        cell.setCellValue((Integer) obj);
                }
            }
            wb.write(fileOut);
            fileOut.close();
        }
    }

    /**
     * Parse header annotation from scripts file.
     */
    public static Map<String, Object[]> parseAnnotations() throws Exception {
        String filePath = System.getProperty("user.dir")+"/target/test-classes/com/swg/lab/testScripts";
        List<String> classes;
        //getting the list of compiled classes from build where all the compiled classes will be kept inside specific Automation project.
        String path = System.getProperty("user.dir");
        classes = Util.getClassFiles(filePath); //Login

        System.out.println("Path is::: "+classes);

        //Loading each classes and finding the header information from it.
        for (String className : classes) {
            Class<?> obj = null;
            try {
                obj = Class.forName(className);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Process @HeaderData
            if (obj != null && obj.isAnnotationPresent(HeaderData.class)) {
                //getting the script name which contains the headers.
                String testScriptName = className.substring(className.lastIndexOf(".")).replace(".", "");
                String[] splitString = className.split("\\.");
                String featureGroupName = "";
                boolean isFeature = false;
                //getting the feature group name which contains the headers.
                for (int i = 0; i < splitString.length - 1; i++) {

                    if (!splitString[i].equals("testScripts") && !isFeature) {
                        continue;
                    } else if (isFeature) {
                        if (featureGroupName.equals("")) {
                            featureGroupName += splitString[i];
                        } else {
                            featureGroupName += "." + splitString[i];
                        }
                    } else {
                        isFeature = true;

                    }
                }
                Annotation annotation = obj.getAnnotation(HeaderData.class);
                HeaderData headerData = (HeaderData) annotation;
                //Extract information from script header annotation to update xlsx file
                data = extractAnnotationInfo(headerData, featureGroupName, testScriptName);
                System.out.println("Data :"+data);

            }
        }
        return data;
    }

    /**
     * Extract information from script header annotation to update xlsx file.
     *
     * @param headerData script information header annotation.
     */
    public static Map<String, Object[]> extractAnnotationInfo(HeaderData headerData, String featureGroupName,
                                                              String testScriptName) {
        String androidStatus = "No", iosStatus = "No", suiteName = "";
        //getting all the information from scripts headers.
        String testScriptDescription = headerData.testScriptDescription();
        String testCaseId = headerData.testCaseId();
        String requirementId = headerData.requirementID();
        List<HeaderData.ExecutableFor> status = Arrays.asList(headerData.executableFor());
        List<HeaderData.Configuration> testConfiguration = Arrays.asList(headerData.configuration());
        List<HeaderData.Suite> testSuite = Arrays.asList(headerData.suite());
        for (HeaderData.Suite eachTestSuite : testSuite) {
            if (testSuite.indexOf(eachTestSuite) == testSuite.size() - 1) {
                suiteName = suiteName + eachTestSuite.toString();
            } else {
                suiteName = suiteName + eachTestSuite.toString() + ",";
            }
        }

        for (HeaderData.ExecutableFor executionStatus : status) {
            if (executionStatus.toString().equalsIgnoreCase("ANDROID")) {
                androidStatus = "Yes";
            }
            if (executionStatus.toString().equalsIgnoreCase("IOS")) {
                iosStatus = "Yes";
            }
        }

        String configuration = featureGroupName + ";" + testScriptName + ";" + testScriptDescription + ";" + testCaseId + ";" + requirementId;
        if (testConfiguration.toString().contains("SWGLAB_IOS")) {
            configuration = configuration + ";Yes;" + iosStatus;
        } else {
            configuration = configuration + ";No;" + iosStatus;
        }
        if (testConfiguration.toString().contains("SWGLAB_ANDROID")) {
            configuration = configuration + ";Yes;" + androidStatus;
        } else {
            configuration = configuration + ";No;" + androidStatus;
        }
        configuration = configuration + ";;" + suiteName + ";";
        Object[] objects = configuration.split(";");
        data.put(testScriptName, objects);
        return data;
    }


    /**
     * Writes data in xls file
     *
     * @param data Map
     * @throws Exception
     */
    private static void writeInExcel(Map<String, Object[]> data) throws Exception {
        File myFile = new File(ExcelUtil.getTestSuite());
        FileInputStream fis = new FileInputStream(myFile);
        XSSFWorkbook book = new XSSFWorkbook(fis);
        XSSFSheet sheet = book.getSheetAt(0);
//        List<String> header = getHeader(sheet);

        CellStyle dataBorderStyle = ExcelUtil.getCellBorderStyle(book);
        //Iterate over data and write to sheet
        Set<String> newRows = data.keySet();
        Row row;
        for (String scriptName : newRows) {
            if (key.contains(scriptName)) {
                row = sheet.createRow(key.indexOf(scriptName) + 2);
            } else {
                row = sheet.createRow(sheet.getLastRowNum() + 1);
            }
            Object[] objArr = data.get(scriptName);
            int cellNum = 0;
//            int iosIndex = header.indexOf("IOS");
            for (Object obj1 : objArr) {
                Cell cell = row.createCell(cellNum++);
                if (obj1 instanceof String) {
                    cell.setCellValue((String) obj1);
                    System.out.println("Cell String value :: "+ obj1);
                    cell.setCellStyle(dataBorderStyle);
                } else if (obj1 instanceof Boolean) {
                    cell.setCellValue((Boolean) obj1);
                    System.out.println("Cell Boolean value :: "+ obj1);
                    cell.setCellStyle(dataBorderStyle);
                } else if (obj1 instanceof Date) {
                    cell.setCellValue((Date) obj1);
                    System.out.println("Cell Date value :: "+ obj1);
                    cell.setCellStyle(dataBorderStyle);
                } else if (obj1 instanceof Double) {
                    cell.setCellValue((Double) obj1);
                    System.out.println("Cell Double value :: "+ obj1);
                    cell.setCellStyle(dataBorderStyle);
                }
            }
        }
        FileOutputStream fileOut = new FileOutputStream(myFile);
        book.write(fileOut);
        fileOut.close();
    }

    /**
     * Read data from the sheet and add it to Map
     *
     * @return testRecordMap
     * @throws Exception
     */
    private static LinkedHashMap<String, Object[]> getSuiteMap() throws Exception {
        XSSFSheet sheet = ExcelUtil.getSheet(ExcelUtil.getTestSuite());
        List<String> header = getHeader(sheet);
        LinkedHashMap<String, Object[]> testRecordMap = new LinkedHashMap<>();

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            String testRecord = "";
            Row row = sheet.getRow(i);
            for (int c = 0; c <= header.indexOf("Suite Name") + 1; c++) {
                Cell cell = row.getCell(c);
                if (cell != null) {
                    if (c == 0) {
                        testRecord = cell.getStringCellValue();
                    } else {
                        testRecord = testRecord + ";" + cell.getStringCellValue();
                    }
                } else {
                    testRecord = testRecord + ";" + " ";
                }
            }
            Cell cell = row.getCell(header.indexOf("Test Script"));
            Object[] objects = testRecord.split(";");
            testRecordMap.put(cell.getStringCellValue(), objects);
        }
        return testRecordMap;
    }


    /**
     * Update suite data in xls.
     *
     * @throws Exception
     */
    public static void updateSuiteXLS() throws Exception {

        XSSFSheet sheet = ExcelUtil.getSheet(ExcelUtil.getTestSuite());
        if (sheet.getLastRowNum() < 2) {
            parseAnnotations();
            writeInExcel(data);
        } else {
            parseAnnotations();
            Map<String, Object[]> testRecordMap = data;
            LinkedHashMap<String, Object[]> testSuiteXLSMap = getSuiteMap();
            Map<String, Object[]> finalSuiteMap = new HashMap<String, Object[]>();
            Map<String, Object[]> updateSuiteMap = new HashMap<String, Object[]>();
//            List<String> header = getHeader(sheet);
            key = new ArrayList(testSuiteXLSMap.keySet());
            dataKey = new ArrayList(testRecordMap.keySet());
            for (String script : key) {
                if (testRecordMap.get(script) == null) {
                    removeRow(sheet, findRow(sheet, script));

                    FileOutputStream fileOut = new FileOutputStream(ExcelUtil.getTestSuite());
                    sheet.getWorkbook().write(fileOut);
                    fileOut.close();
                } else {
                    for (int i = 0; i < testRecordMap.get(script).length; i++) {
                        Object[] rec = testRecordMap.get(script);
                        Object[] data = testSuiteXLSMap.get(script);
                        if (i != 9) {
                            if (!rec[i].equals(data[i])) {
                                updateSuiteMap.put(script, rec);
                            }
                        }
                    }
                }
            }
            updateExcel(updateSuiteMap);
            for (String newScript : dataKey) {
                if (!key.contains(newScript)) {
                    finalSuiteMap.put(newScript, data.get(newScript));
                }
            }
            writeInExcel(finalSuiteMap);
        }
    }

    /**
     * Gets header data from suite sheet
     *
     * @param sheet suite
     * @return headerList
     */
    private static List<String> getHeader(XSSFSheet sheet) {
        List<String> headerList = new ArrayList<String>();
        Row header = sheet.getRow(0);
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            if (cell == null) {
                headerList.add("");
            } else {
                headerList.add(cell.getStringCellValue());
            }
        }
        return headerList;
    }

    /**
     * Update existing records in xls     *
     *
     * @param data Map
     * @throws Exception
     */
    private static void updateExcel(Map<String, Object[]> data) throws Exception {
        File myFile = new File(ExcelUtil.getTestSuite());
        FileInputStream fis = new FileInputStream(myFile);
        XSSFWorkbook book = new XSSFWorkbook(fis);
        XSSFSheet sheet = book.getSheetAt(0);
//        List<String> header = getHeader(sheet);

        //Iterate over data and write to sheet
        Set<String> newRows = data.keySet();
        XSSFRow row;
        for (String scriptName : newRows) {
            if (key.contains(scriptName)) {
                row = sheet.getRow(key.indexOf(scriptName) + 2);
            } else {
                row = sheet.getRow(sheet.getLastRowNum() + 1);
            }
            Object[] objArr = data.get(scriptName);
            int cellNum = 0;
//            int iosIndex = header.indexOf("IOS");
            for (Object obj1 : objArr) {
                XSSFCell cell = row.getCell(cellNum++);
//                if (cellNum != iosIndex + 2) {
                if (obj1 instanceof String) {
                    cell.setCellValue((String) obj1);
                } else if (obj1 instanceof Boolean) {
                    cell.setCellValue((Boolean) obj1);
                } else if (obj1 instanceof Date) {
                    cell.setCellValue((Date) obj1);
                } else if (obj1 instanceof Double) {
                    cell.setCellValue((Double) obj1);
                }
//                }
            }
        }
        FileOutputStream fileOut = new FileOutputStream(myFile);
        book.write(fileOut);
        fileOut.close();
    }

    /**
     * This is the method to find the row number
     */
    private static int findRow(XSSFSheet sheet, String cellContent) {
        int rowNum = 0;

        for (Row row : sheet) {
            for (Cell cell : row) {
                while (cell.getStringCellValue().equals(cellContent)) {
                    if (Objects.equals(cell.getRichStringCellValue().getString(), cellContent)) {
                        rowNum = row.getRowNum();
                        return rowNum;
                    }
                }
            }
        }
        return rowNum;
    }

    /**
     * Remove a row by its index
     *
     * @param sheet    a Excel sheet
     * @param rowIndex a 0 based index of removing row
     */
    public static void removeRow(XSSFSheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            XSSFRow removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }
}

