package com.swg.framework.logging;

import com.swg.framework.common.Constants;
import com.swg.framework.setup.InitializerScript;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Logger class.
 */
public class FrameworkLogger{

    //public static String LOGFILENAME;
    public static ThreadLocal<String> LOGFILENAME = new ThreadLocal<>();

    //static StringBuilder loggerText;
    public static ThreadLocal<StringBuilder> loggerText = new ThreadLocal<>();

    private static String currentTestClassName;
    private static String LINE_SEP = " - ";
    private static String INFO = " INFO ";
    private static String PASS = " PASS ";
    private static String FAIL = " FAIL ";
    private static String ERROR = " ERROR ";
    private static String WARNING = " WARNING ";



    /**
     * Method to add steps in logger
     */
    public static void logStep(String message) {
        loggerText.get().append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + INFO + LINE_SEP);
        loggerText.get().append(message + "\n");
    }

    /**
     * Method to add error message in logger and exit application
     */
    public static void logError(String exception) {
        if (exception != null) {
            loggerText.get().append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + ERROR + LINE_SEP);
            loggerText.get().append(exception + "\n");
        }
        System.exit(0);
    }

    /**
     * Method to add warning in logger
     */
    public static void logWarning(String message) {
        loggerText.get().append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + WARNING + LINE_SEP);
        loggerText.get().append(message + "\n");
    }

    //This method is used when verification statement fails
    public static void logFail(final String failMessage) {
        loggerText.get().append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + FAIL + LINE_SEP);
        loggerText.get().append(failMessage + "\n");
    }

    //This method is used when verification is pass
    public static void logPass(final String verifyMessage) {
        loggerText.get().append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + PASS + LINE_SEP);
        loggerText.get().append(verifyMessage + "\n");
    }

    private static Map<String, String> getClassHeaderData(final Class<? extends InitializerScript> testClass) {

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        Map<String, String> logHeaderDataMap = new LinkedHashMap<String, String>();

        logHeaderDataMap.put("Current DateTime", currentDate);
        logHeaderDataMap.put("Script Name", testClass.getSimpleName());

        return logHeaderDataMap;
    }

    public static void config(String requirement, String TeatCase, String scriptName, String deviceName, String description)
            throws IOException {

        loggerText.set(new StringBuilder());

        //Create log file name
        String currentDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
        StringBuilder logfile_name = new StringBuilder(Constants.REPORT_PATH);
        //LOGFILENAME = logfile_name.toString() + scriptName + "_" + deviceName + ".txt";
        LOGFILENAME.set(logfile_name.toString() + scriptName + "_" + deviceName + ".txt");

        loggerText.get().append("DATE : " + currentDate);
        loggerText.get().append(System.getProperty("line.separator"));
        loggerText.get().append("Script : " + scriptName);
        loggerText.get().append(System.getProperty("line.separator"));
        loggerText.get().append("Requirement ID : " + requirement);
        loggerText.get().append(System.getProperty("line.separator"));
        loggerText.get().append("Test Case ID : " + TeatCase);
        loggerText.get().append(System.getProperty("line.separator"));
        loggerText.get().append("Description : " + description);
        loggerText.get().append(System.getProperty("line.separator"));
        loggerText.get().append("Device : " + deviceName);
        loggerText.get().append(System.getProperty("line.separator"));
        loggerText.get().append(System.getProperty("line.separator"));
        loggerText.get().append("******************************************************************************************");
        loggerText.get().append(System.getProperty("line.separator"));
        loggerText.get().append(System.getProperty("line.separator"));


    }

    public static void writeLog() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(LOGFILENAME.get()));
            System.out.println("Writing the file : " + LOGFILENAME.get());
            writer.write(loggerText.get().toString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error while writing log");
        }

    }

    public static String captureImage(Integer screenCaptureIndex) {
        return String.format("%s_%s.png", currentTestClassName, screenCaptureIndex);
    }
//
//    @Override
//    public void onTestStart(ITestResult iTestResult) {
//        config();
//    }
//
//    @Override
//    public void onTestSuccess(ITestResult iTestResult) {
//
//    }
//
//    @Override
//    public void onTestFailure(ITestResult iTestResult) {
//
//    }
//
//    @Override
//    public void onTestSkipped(ITestResult iTestResult) {
//
//    }
//
//    @Override
//    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
//
//    }
//
//    @Override
//    public void onStart(ITestContext iTestContext) {
//
//    }
//
//    @Override
//    public void onFinish(ITestContext iTestContext) {
//
//    }
}
