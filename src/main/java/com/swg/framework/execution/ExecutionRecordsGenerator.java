package com.swg.framework.execution;

import com.swg.framework.api.adb.AdbHelper;
import com.swg.framework.common.Constants;
import com.swg.framework.utility.ExcelUtil;
import com.swg.framework.utility.Util;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Creating records file for execution
 */
public class ExecutionRecordsGenerator {
    static String rootPath = System.getProperty("user.dir");
    static List<String> headerList;
    static String suite_to_execute = "";
    static String features_to_execute = "";
    static Map<String, List<String>> configurationMap = new HashMap<>();
    private static String ANDROID_PACKAGE = "com.swg.lab.testScripts.";
    private static String IOS_PACKAGE = "com.swg.lab.iOScripts.";
    static int recordNumber = 1;
    static int number = 0;
    static List<String> getScriptNames = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        setTestSuiteName();
        getTestConfiguration();
        generateRecords();

    }

    /**
     * Read data from the sheet and add it to Map
     *
     * @return testRecordMap
     * @throws Exception
     */
    private static Map<String, Object[]> getSuiteMap() throws Exception {
        XSSFSheet sheet = ExcelUtil.getSheet(ExcelUtil.getTestSuite());
        List<String> header = getHeader(sheet);
        Map<String, Object[]> testRecordMap = new TreeMap<>();

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            String testRecord = "";
            Row row = sheet.getRow(i);
            for (int c = 0; c <= header.indexOf("Suite Name") + 3; c++) {
                Cell cell = row.getCell(c);
                if (cell != null) {
                    if (c == 0) {
                        testRecord = cell.getStringCellValue();
                    } else {
                        testRecord = testRecord + ";" + cell.getStringCellValue();
                    }
                } else {
                    if (!testRecord.contentEquals("")) {
                        testRecord = testRecord + ";" + " ";
                    }
                }
            }
            Cell cell = row.getCell(header.indexOf("Test Script"));
            if (!testRecord.contentEquals(";;;;;;;;;;;") || !testRecord.contentEquals(";;;;;;;;;;;;")) {
                Object[] objects = testRecord.split(";");
                if (!objects[1].toString().isEmpty()) {
                    testRecordMap.put(cell.getStringCellValue(), objects);
                }
            }
        }
        return testRecordMap;
    }

    /**
     * Gets header data from suite sheet
     *
     * @param sheet suite
     * @return headerList
     */
    private static List<String> getHeader(XSSFSheet sheet) {
        headerList = new ArrayList<>();

        Row header = sheet.getRow(0);
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            if (cell == null) {
                headerList.add("");
            } else {
                headerList.add(cell.getStringCellValue());
                System.out.println("Cell Value is ---- "+ cell.getStringCellValue());
            }
        }
        return headerList;
    }

    /**
     * Gets suite to execute and Feature to execute data
     *
     * @param sheet : Excel Sheet object
     */
    private static void getSuiteFeatureData(XSSFSheet sheet) {
        getHeader(sheet);
        Row row = sheet.getRow(0);
        if (row.getCell(headerList.indexOf("Suite to execute") + 1) == null) {
            suite_to_execute = "";
        } else {
            suite_to_execute = row.getCell(headerList.indexOf("Suite to execute") + 1).getStringCellValue();
            System.out.println("suite_to_execute -- "+suite_to_execute);
        }
        row = sheet.getRow(1);
        if (row.getCell(headerList.indexOf("Suite to execute") + 1) == null) {
            features_to_execute = "";
        } else {
            features_to_execute = row.getCell(headerList.indexOf("Suite to execute") + 1).getStringCellValue();
            System.out.println("features_to_execute -- "+features_to_execute);
        }
    }

    /**
     * Generate Execution records
     *
     * @throws Exception
     */
    public static void generateRecords() throws Exception {
        getSuiteFeatureData(ExcelUtil.getSheet(ExcelUtil.getTestSuite()));
        boolean suiteExist = true;
        boolean featureExist = true;
        Map<String, Object[]> testRecordMap = getSuiteMap();
        List<String> key = new ArrayList();
        for (String keyset : testRecordMap.keySet()) {
            if (!keyset.contentEquals("")) {
                key.add(keyset);
            }
        }
        int count = 0;
        for (String script : key) {
//            System.out.println( "Get Script "+script);
            Object[] obj = testRecordMap.get(script);
//            System.out.println( "Get Script Object "+obj);
            if (suite_to_execute.equalsIgnoreCase(Constants.ALL_TESTS)) {
                count = count + 1;
            } else if (suite_to_execute.equalsIgnoreCase("") && !features_to_execute.equalsIgnoreCase("")) {
                if (features_to_execute.contains((String) obj[headerList.indexOf("Feature Group")]) &&
                        obj[headerList.indexOf("Final Execution")].equals(Constants.YES)) {
                    count = count + 1;
                }
            } else if (suite_to_execute.equalsIgnoreCase("") && features_to_execute.equalsIgnoreCase("")) {
                if (obj[headerList.indexOf("Final Execution")].equals(Constants.YES)) {
                    count = count + 1;
                }
            } else if (isSuiteExist(obj, suite_to_execute) && obj[headerList.indexOf("Final Execution")].equals(Constants.YES)) {
                count = count + 1;
            }
        }
        for (String script : key) {
            Object[] obj = testRecordMap.get(script);
            if (suite_to_execute.equalsIgnoreCase(Constants.ALL_TESTS)) {
                //checkAndCreateRecords(obj, count);
                checkAndCreateSuite(obj, count);

            } else if (suite_to_execute.equalsIgnoreCase("") && !features_to_execute.equalsIgnoreCase("")) {
                if (features_to_execute.contains((String) obj[headerList.indexOf("Feature Group")]) &&
                        obj[headerList.indexOf("Final Execution")].equals(Constants.YES)) {

                    checkAndCreateSuite(obj, count);
                }
            } else if (suite_to_execute.equalsIgnoreCase("") && features_to_execute.equalsIgnoreCase("")) {
                if (obj[headerList.indexOf("Final Execution")].equals(Constants.YES)) {

                    checkAndCreateSuite(obj, count);
                }
            } else if (isSuiteExist(obj, suite_to_execute) && obj[headerList.indexOf("Final Execution")].equals(Constants.YES)) {
                checkAndCreateSuite(obj, count);
            }

            featureExist = !features_to_execute.equals("") &&
                    ((String) obj[headerList.indexOf("Feature Group")]).contains(features_to_execute);

            suiteExist = !suite_to_execute.equals("") &&
                    ((String) obj[headerList.indexOf("Suite Name")]).contains(suite_to_execute);

            if (featureExist) {
//                System.out.println("Feature does not exist");
            } else if (suiteExist) {
//                System.out.println("Suite does not exist");
            }
        }
    }

    private static boolean isSuiteExist(Object[] record, String suiteName) {
        String[] suites = new String[record.length];
        if ((record[headerList.indexOf("Suite Name")]).toString().contains(",") && !(record[headerList.indexOf("Suite Name")]).toString().isEmpty()) {
            suites = ((String) record[headerList.indexOf("Suite Name")]).split(",");
        } else if (!(record[headerList.indexOf("Suite Name")]).toString().isEmpty()) {
            suites = ((String) record[headerList.indexOf("Suite Name")]).split(",");
        }

        for (String suite : suites) {
            if (suite != null) {
                if (suite.trim().equalsIgnoreCase(suiteName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to write in execution records
     *
     * @param obj :
     */
    private static void checkAndCreateRecords(Object[] obj, int count) throws Exception {
        String executionPlatform = Util.readFile("Project.Properties", "Platform");
        String configurationValue = String.valueOf(configurationMap.get(obj[1]));
        configurationValue = configurationValue.substring(2, configurationValue.length() - 2).trim();
        List<String> configValue = Arrays.asList(configurationValue.split(","));
        String featureName = (String) obj[headerList.indexOf("Feature Group")];
        if (obj[headerList.indexOf("Feature Group")].equals("recordsharing")) {
            featureName = "x" + obj[headerList.indexOf("Feature Group")];
        } else if (obj[headerList.indexOf("Feature Group")].equals("offline")) {
            featureName = "y" + obj[headerList.indexOf("Feature Group")];
        }
        int deviceCount = AdbHelper.deviceName().size();
        int rounds = Math.round(count / deviceCount) + 1;
        String recordPath = "";
        for (int i = 0; i <= rounds; i++) {
            recordPath = "./Records" + recordNumber + "/";
            if (executionPlatform.equals(Constants.IOS)) {
                if (configurationValue.contains("SWGLAB_IOS") && obj[headerList.indexOf(Constants.IOS)].equals(Constants.YES) &&
                        obj[headerList.indexOf(Constants.IOS) + 1].equals(Constants.YES)) {
                    if (!obj[headerList.indexOf(Constants.IOS)].equals(Constants.YES)) {
                        createTextFile(Constants.IOS + "=" + IOS_PACKAGE + obj[headerList.indexOf("Feature Group")] + "." +
                                obj[headerList.indexOf("Test Script")], Constants.IOS + "_" + featureName + "_" + obj[headerList.indexOf("Test Script")], recordPath);
                        number = number + 1;
                        if (number == rounds) {
                            number = 0;
                            recordNumber = recordNumber + 1;
                        }
                        break;
                    } else {
                        createTextFile(Constants.IOS + "=" + IOS_PACKAGE + obj[headerList.indexOf("Feature Group")] + "." +
                                obj[headerList.indexOf("Test Script")], Constants.IOS + "_" + featureName + "_" + obj[headerList.indexOf("Test Script")], recordPath);
                        number = number + 1;
                        if (number == rounds) {
                            number = 0;
                            recordNumber = recordNumber + 1;
                        }
                        break;
                    }
                }
            } else if (executionPlatform.equals(Constants.ANDROID)) {
                if (configurationValue.contains("SWGLAB_ANDROID") && obj[headerList.indexOf(Constants.ANDROID)].equals(Constants.YES) &&
                        obj[headerList.indexOf(Constants.ANDROID) + 1].equals(Constants.YES)) {
                    if (!obj[headerList.indexOf(Constants.ANDROID)].equals(Constants.YES)) {
                        createTextFile(Constants.ANDROID + "=" + ANDROID_PACKAGE + obj[headerList.indexOf("Feature Group")] + "." +
                                obj[headerList.indexOf("Test Script")], Constants.ANDROID + "_" + featureName + "_" + obj[headerList.indexOf("Test Script")], recordPath);
                        number = number + 1;
                        if (number == rounds) {
                            number = 0;
                            recordNumber = recordNumber + 1;
                        }
                        break;
                    } else {
                        createTextFile(Constants.ANDROID + "=" + ANDROID_PACKAGE + obj[headerList.indexOf("Feature Group")] + "." +
                                obj[headerList.indexOf("Test Script")], Constants.ANDROID + "_" + featureName + "_" + obj[headerList.indexOf("Test Script")], recordPath);
                        number = number + 1;
                        if (number == rounds) {
                            number = 0;
                            recordNumber = recordNumber + 1;
                        }
                        break;
                    }
                }
            }

        }

    }

    /**
     * Method to fetch in execution scripts from Mobile suite
     *
     * @param obj :
     */
    private static void checkAndCreateSuite(Object[] obj, int count) throws Exception {

        String executionPlatform = Util.readFile("Project.Properties", "Platform");
        String configurationValue = String.valueOf(configurationMap.get(obj[1]));
        configurationValue = configurationValue.substring(2, configurationValue.length() - 2).trim();
        List<String> configValue = Arrays.asList(configurationValue.split(","));
        String featureName = (String) obj[headerList.indexOf("Feature Group")];
        if (obj[headerList.indexOf("Feature Group")].equals("recordsharing")) {
            featureName = "x" + obj[headerList.indexOf("Feature Group")];
        } else if (obj[headerList.indexOf("Feature Group")].equals("offline")) {
            featureName = "y" + obj[headerList.indexOf("Feature Group")];
        }
        int deviceCount = AdbHelper.deviceName().size();
        int rounds = Math.round(count / deviceCount) + 1;
        String recordPath = "";
        String body;
        for (int i = 0; i <= rounds; i++) {
            recordPath = "./Records" + recordNumber + "/";
            if (executionPlatform.equals(Constants.IOS)) {
                if (configurationValue.contains("SWGLAB_IOS") && obj[headerList.indexOf(Constants.IOS)].equals(Constants.YES) &&
                        obj[headerList.indexOf(Constants.IOS) + 1].equals(Constants.YES)) {
                    if (!obj[headerList.indexOf(Constants.IOS)].equals(Constants.YES)) {
                        body = Constants.IOS + "=" + IOS_PACKAGE + obj[headerList.indexOf("Feature Group")] + "." +
                                obj[headerList.indexOf("Test Script")] + "  " + Constants.IOS + "_" + featureName + "_" + obj[headerList.indexOf("Test Script")] + " " + recordPath;
                        addScript(body);
                        number = number + 1;
                        if (number == rounds) {
                            number = 0;
                            recordNumber = recordNumber + 1;
                        }
                        break;
                    } else {


                        body = Constants.IOS + "=" + IOS_PACKAGE + obj[headerList.indexOf("Feature Group")] + "." +
                                obj[headerList.indexOf("Test Script")] + " " + Constants.IOS + "_" + featureName + "_" + obj[headerList.indexOf("Test Script")] + " " + recordPath;

                        addScript(body);
                        number = number + 1;
                        if (number == rounds) {
                            number = 0;
                            recordNumber = recordNumber + 1;
                        }
                        break;
                    }
                }
            } else if (executionPlatform.equals(Constants.ANDROID)) {
                if (configurationValue.contains("SWGLAB_ANDROID") && obj[headerList.indexOf(Constants.ANDROID)].equals(Constants.YES) &&
                        obj[headerList.indexOf(Constants.ANDROID) + 1].equals(Constants.YES)) {
                    if (!obj[headerList.indexOf(Constants.ANDROID)].equals(Constants.YES)) {

                        body = ANDROID_PACKAGE + obj[headerList.indexOf("Feature Group")] + "." + obj[headerList.indexOf("Test Script")];
                        addScript(body);
                        number = number + 1;
                        if (number == rounds) {
                            number = 0;
                            recordNumber = recordNumber + 1;
                        }
                        break;
                    } else {

                        body = ANDROID_PACKAGE + obj[headerList.indexOf("Feature Group")] + "." + obj[headerList.indexOf("Test Script")];
                        addScript(body);
                        number = number + 1;
                        if (number == rounds) {
                            number = 0;
                            recordNumber = recordNumber + 1;
                        }
                        break;
                    }
                }
            }

        }
    }

    /**
     * This method creates text file
     *
     * @param body     content in the file
     * @param fileName file name
     */
    private static void createTextFile(String body, String fileName, String path) {

        try {
            File file = new File(path + fileName + ".txt");
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    System.out.println("File not created.");
                }
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            getScriptNames.add(fileName);
            bw.write(body);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void addScript(String body) {
        getScriptNames.add(body);

    }

    /**
     * Method to get test configuration
     */
    public static void getTestConfiguration() {
        String filePath = System.getProperty("user.dir") + "/target/test-classes/com/swg/lab/testScripts";
        //Loading each classes and finding the header information from it.
        List<String> classes = Util.getClassFiles(filePath);

        System.out.println("Path is::: " + classes);
        for (String className : classes) {
            Class<?> obj = null;
            try {
                obj = Class.forName(className);
                System.out.println("Class Name::: "+obj);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (obj != null && obj.isAnnotationPresent(HeaderData.class)) {

                //getting the script name which contains the headers.
                String testScriptName = className.substring(className.lastIndexOf(".")).replace(".", "");
                Annotation annotation = obj.getAnnotation(HeaderData.class);
                HeaderData headerData = (HeaderData) annotation;

                List<String> testConfiguration = Arrays.asList(Arrays.toString(headerData.configuration()));

                configurationMap.put(testScriptName, testConfiguration);
            }
        }
    }

    /**
     * Write test results in result sheet
     *
     * @throws Exception
     */
    public static void setTestSuiteName() throws Exception {
        String suiteName = Util.readFile("Project.Properties", "TestSuiteName");
        if (suiteName == null || suiteName.isEmpty()) {
            suiteName = "";
        }
        String path = System.getProperty("user.dir").trim();
        File excel = new File(path + "/TestSuite/MobileTestSuite.xlsx");
        FileInputStream fis = new FileInputStream(excel);
        XSSFWorkbook book = new XSSFWorkbook(fis);
        XSSFSheet sheet = book.getSheet("SuiteFile");

        Row row = sheet.getRow(0);
        Cell cell = row.createCell(12);
        cell.setCellValue(suiteName);
        // open an OutputStream to save written data into Excel file
        FileOutputStream os = new FileOutputStream(excel);
        book.write(os);
        // Close workbook, OutputStream and Excel file to prevent leak
        os.close();
        fis.close();
    }

    public static void recursiveDelete(File file) throws IOException {
        //to end the recursive loop
        if (!file.exists())
            return;
        List<String> filePath = findFoldersInDirectory(rootPath);
        for (int i = 0; i < filePath.size(); i++) {
            File file1 = new File(rootPath + "/" + filePath.get(i));
            //if directory, go inside and call recursively
            if (file1.isDirectory()) {
                //call recursively
                FileUtils.deleteDirectory(file1);
            }
        }
    }

    public static List<String> findFoldersInDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        FileFilter directoryFileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        List<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            if (directoryAsFile.toString().contains("Records")) {
                foldersInDirectory.add(directoryAsFile.getName());
            }
        }

        return foldersInDirectory;
    }

    public static void createDirectories() throws Exception {
        int deviceCount = AdbHelper.deviceName().size();
        for (int i = 1; i <= deviceCount; i++) {
            File file = new File(rootPath + "/Records" + i);
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Directory is created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }
        }
    }

    public static List<String> updatedSuiteCreator() throws Exception {
        return getScriptNames;
    }
}
