package com.swg.framework.setup;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.swg.framework.api.adb.AdbHelper;
import com.swg.framework.common.Constants;
import com.swg.framework.datadriven.model.DataContainer;
import com.swg.framework.datadriven.model.TestDataRecord;
import com.swg.framework.datadriven.reader.*;
import com.swg.framework.enums.MobCapabilityType;
import com.swg.framework.execution.ExcelResultHelper;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.utility.Util;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
import org.testng.annotations.Optional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.swg.framework.logging.FrameworkLogger.loggerText;

/**
 * Base test script for common activities for every script.
 */
   /*
    Implemented below changes so far for synchronizing logs generation:
    ################


     1) In initializerscript used ThreadLocal<AppiumDriver> driver , instead of static AppiumDriver driver (Alternatively, we can also use the ThreadLocalDriver class and its method)
     2) In FWLogger class set, Threadlocal LOGFILENAME and updated get set methods , changesmade in initializer script asw well
     3) In FWLogger class set, ThreadLocal loggerText
     4) moved code of after suite to after method
     5) in @Test used just invokeTest() method
     6) in initializerscript deviceModelName has been made threadlocal
     7) In initializerscript status has been made ThreadLocal,  which is to be tested yet if requirement

08 nov2019
     8)made device, port and platform as threadlocal variable too and modified its usage across fmwk
     9)commented setDevice(port) under setDriver method
     */

public class InitializerScript {

    public static HashMap<Long, String> threadDevice = new HashMap<>();
    public static final String packageName = "com.swaglabsmobileapp";
    protected static final String activityName = "com.swaglabsmobileapp.MainActivity";
    public static final String portValueParam = "portName";
    public static final String deviceIDParam = "deviceIDName";
    public static final String platformVersionNameParam = "platformVersionName";
    public static ThreadLocal<String> platform =  new ThreadLocal<>();
    public static ThreadLocal<String> device =  new ThreadLocal<>();
    protected static ThreadLocal<String> port = new ThreadLocal<>();
    public static ThreadLocal<String> deviceModelName = new ThreadLocal<>();
    public static String testCaseID = "";
    public static Map<String, String> resultMap = new HashMap<>();
    public static Integer implicitTimeOut = 30;
    public static List<String> deviceName;
    public static ThreadLocal<Boolean> status =
            new ThreadLocal<Boolean>() {
                @Override
                public Boolean initialValue() {
                    return true;
                }
            };
    public static boolean isResetRequired = false;
    public static boolean isScreenShotCaptured = false;
    public static boolean isMobile;
    public static long currentTime;
    private static String strLocale = getLocale();
    public static Map<String, String> resourceMap = getResourceMap(strLocale);
    static ExtentTest test;
    static ExtentReports report;
    public static Map<String, Integer> failedTC = new HashMap<String, Integer>();
    int failedTCCount = 1;
    //protected static AppiumDriver driver;
    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    protected static IOSDriver iosDriver;
    protected static WebDriver webDriver;
    protected static String[] allPorts = StartAppiumServer.port;

    protected static String bundleID = "";
    private static AndroidDriver androidDriver;
    private static String commandTimeOut = "60";
    private static String className;
    private static File apkFilePath;
    private static File ipaFilePath;
    private static File chromeDriverPath;
    private static List<String> osVersion;
    private static List<String> deviceModelNameList;
    private static List<String> testID;
    private static boolean isDebug = false;

    private DataContainer dataContainer;

    StartAppiumServer startAppiumServer = new StartAppiumServer();

    public InitializerScript() {
        //screenFactory = new ScreenFactory();
        startAppiumServer.launchServer();
    }

    @BeforeSuite
    //@BeforeClass
    public void beforeClass() throws Exception {
        System.out.println("******In BeforeClass *********************");
        isDebug = Boolean.parseBoolean(Util.readFile("Project.Properties", "isDebugMode"));
        deviceName = Arrays.asList(Util.readFile("DeviceInfo.Properties", "devName").split(","));
        osVersion = Arrays.asList(Util.readFile("DeviceInfo.Properties", "osVersion").split(","));
        deviceModelNameList = Arrays.asList(Util.readFile("DeviceInfo.Properties", "deviceModel").split(","));
        isMobile = Boolean.parseBoolean(Util.readFile("Project.Properties", "isMobileAutomation"));

        /*Created instance of extent report and provided the path of HTML report file */
        report = new ExtentReports(System.getProperty("user.dir") + "/Reports/ExecutionReportResults.html");

        /*This will display the execution environment details Tabular view on extent report dashboard*/
        report.addSystemInfo("Environment : ", Util.readProperties("executionServer"));
        report.addSystemInfo("Java Version : ", "java.version");
        report.addSystemInfo("Platform : ", Util.readFile("Project.Properties", "Platform"));
        report.addSystemInfo("Device Model : ", Util.readFile("DeviceInfo.Properties", "deviceModel"));
        report.addSystemInfo("Device OS Version : ", Util.readFile("DeviceInfo.Properties", "osVersion"));
        report.addSystemInfo("Device Name : ", Util.readFile("DeviceInfo.Properties", "devName"));
        report.addSystemInfo("HealthHub Application Version : ", AdbHelper.getAndroidBuildVersion(deviceName));
    }

    //changed @Before to @BeforeMethod of Testng
    @BeforeMethod
    //@BeforeTest
    @Parameters({deviceIDParam, platformVersionNameParam, portValueParam})
    //@Parameters({"deviceIDName", "platformVersionName", "portName"})
    public void setup(@Optional String deviceIDName, @Optional String platformVersionName, @Optional String portName) throws Exception {
        currentTime = System.currentTimeMillis();
        if (deviceIDName != null) {
            System.out.println("Setting device, platform and port by parameter");
            threadDevice.put(Thread.currentThread().getId(), deviceIDName);
            System.out.println(threadDevice.toString());
            device.set(deviceIDName);
            platform.set(platformVersionName);
            port.set(portName);
        } else {
            System.out.println("Setting device, platform and port by first device connected");
            device.set(AdbHelper.deviceName().get(0));
            platform .set(AdbHelper.deviceVersion(AdbHelper.deviceName()).get(0));
            port.set(StartAppiumServer.port[0]);
        }
        if (isMobile) {
            initializeAppiumDriver(device.get(), platform.get(), port.get());
            switch (port.get()) {
                case "4723":
//                     loginThroughPort(phoneNumber(0, number), otpNumber(0, number));
                    break;
                case "4727":
                    //  loginThroughPort(phoneNumber(2, number), otpNumber(2, number));
                    break;
                case "4731":
                    //  loginThroughPort(phoneNumber(4, number), otpNumber(4, number));
                    break;
                case "4735":
                    //  loginThroughPort(phoneNumber(6, number), otpNumber(6, number));
                    break;
                case "4739":
                    //  loginThroughPort(phoneNumber(8, number), otpNumber(8, number));
                    break;
            }
        }
    }


    /**
     * Specifies a test
     */
    @Test
    public final void test() throws Exception {
        System.out.println("******Start**********test***********" + device.get());
        try {
            //This method will perform Login/Signup Operation.
            configuration();
            invokeTest();

        } catch (Exception e) {

            FrameworkLogger.logFail(e.getMessage());
            e.printStackTrace();
            if (isAndroid()) {
                captureScreenshot();
            } else {
                String path = System.getProperty("user.dir");
                String snapshotPath = path + FrameworkLogger.LOGFILENAME.get().substring(1).replaceAll(".txt", ".png").
                        replaceAll("Logs/", "Logs/Snapshots/");
                AdbHelper.iosCaptureScreen(snapshotPath, device.get());
            }
            status.set(false);
            isScreenShotCaptured = true;
        }

    }

    //changed @After to AfterMethod of Testng
    @AfterMethod
    //@AfterTest
    public void tearDown() throws Exception {
        System.out.println("******Start**********tearDown***********");
        try {

            String scriptStatusFile = deviceModelName.get() + ".Properties";
            String log_text = loggerText.get().toString();
            for (String eachTestId : testID) {
                test = report.startTest(this.getClass().getSimpleName() + "_" + eachTestId);
                eachTestId = eachTestId.trim();
                if (resultMap.containsKey(eachTestId)) {
                    Util.setProjectProperty(scriptStatusFile, eachTestId, resultMap.get(eachTestId));
                    System.out.println("Script Status file:: "+scriptStatusFile + " Each Test Id: "+eachTestId+ " Result Map:: "+resultMap.get(eachTestId));
                    if (resultMap.get(eachTestId).contains("Pass")) {
                        /*Logging the execution output as pass on extent report*/
                        test.log(LogStatus.PASS, eachTestId, log_text);

                    } else {
                        /*Logging the execution output as Fail and provid the failure screenshot on extent report*/
                        byte[] fileContent = FileUtils.readFileToByteArray(new File(captureScreenshot()));
                        String Base64StringofScreenshot = "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
                        test.log(LogStatus.FAIL, eachTestId + "  " + test.addBase64ScreenShot(Base64StringofScreenshot), log_text);
                    }
                } else if (status.get()) {
                    test.log(LogStatus.PASS, "Test pass");
                    Util.setProjectProperty(scriptStatusFile, eachTestId, "Pass");
                    System.out.println("Script Status file:: "+scriptStatusFile + " Each Test Id: "+eachTestId+ " Result Map:: Pass");
                } else {
                    test.log(LogStatus.FAIL, "Test Failed");
                    Util.setProjectProperty(scriptStatusFile, eachTestId, " Fail");
                    System.out.println("Script Status file:: "+scriptStatusFile + "Each Test Id: "+eachTestId+ " Result Map:: Fail");
                       /*
                    Adding failed test cases into map, map is used to get unique failed test script name
                    */
                    failedTC.put(this.getClass().getSimpleName(), failedTCCount);
                    failedTCCount++;
                    if (!isScreenShotCaptured) {
                        captureScreenshot();
                    }
                }
            }

            Util.setProjectProperty(scriptStatusFile, "testName", this.getClass().getSimpleName());
            String logPath = "logFilePath";
            Util.setProjectProperty(scriptStatusFile, logPath, FrameworkLogger.LOGFILENAME.get());

            long totalExecutionTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - currentTime);
            if (totalExecutionTime > 59) {
                totalExecutionTime = TimeUnit.SECONDS.toMinutes(totalExecutionTime);
                FrameworkLogger.logStep(String.format("It took %s minutes for script execution", totalExecutionTime));
            } else {
                totalExecutionTime = TimeUnit.SECONDS.toSeconds(totalExecutionTime);
                FrameworkLogger.logStep(String.format("It took %s seconds for script execution", totalExecutionTime));
            }

            FrameworkLogger.writeLog();
            ExcelResultHelper.updateTestCasesResultInExcel();


            //## copied from after suite
            Util.setProjectProperty("Project.Properties", "isServerRestartRequired", "true");
            if (driver.get() == null && webDriver == null) {
                FrameworkLogger.logFail("Driver is Null");
            } else {
                System.out.println("Quitting Driver");
                if (isAndroid()) {
                    driver.get().quit();
                } else if (isIOS()) {
                    closeServer();
                } else {
                    webDriver.quit();
                }
            }

            System.out.println("******End**********tearDown***********");
        } catch (Exception e) {
            FrameworkLogger.logFail(e.getMessage());
            e.printStackTrace();
        }
    }


    @AfterSuite
    //@AfterClass
    public static void afterClass() throws Exception {
        System.out.println("******In After Class *********************");
        /*Ending and Flushing the report */
        report.endTest(test);
        report.flush();
    }


    protected void configuration() throws Exception {
    }


    public static boolean isAndroid() throws Exception {
        return Util.readProperties("Platform").equals("Android");
    }

    public static boolean isWeb() throws Exception {
        return Util.readProperties("Platform").equals("Web");
    }

    public static boolean isIOS() throws Exception {
        return Util.readProperties("Platform").equals("IOS");
    }


    /**
     * Method to get Appium Driver
     *
     * @return : appium driver
     */
    public static AppiumDriver getDriver() {

        return driver.get();
    }

    /**
     * Method to get Appium Driver
     *
     * @return : appium driver
     */
    public static WebDriver getWebDriver() {


        return webDriver;
    }

    /**
     * Method to get Android Driver
     *
     * @return : android driver
     */
    public static AndroidDriver getAndroidDriver() {
        return androidDriver;
    }

    /**
     * Method to get IOS Driver
     *
     * @return : ios driver
     */
    public static IOSDriver getIOSDriverDriver() {
        return iosDriver;
    }

    /**
     * Method to close Appium Server
     *
     * @throws Exception
     */
    private static void closeServer() throws Exception {
        List<String> list = Util.iosRunProcess(true, "lsof -i :" + port);
        String pID = list.get(1).replaceAll(" ", "").substring(4, 9);
        Util.iosRunProcess(true, "kill " + pID);
        Util.setProjectProperty("Project.Properties", "isServerRestartRequired", "true");
        Util.iosRunProcess(true, "rm -rf /tmp/" + deviceModelName.get().replace("_", "")
                + deviceModelNameList.indexOf(deviceModelName.get().replace("_", " ")));
    }





    /**
     * Method to capture screenshot on failure.
     */
    public static String captureScreenshot() throws Exception {
        String path = System.getProperty("user.dir");
        String snapshotPath = path + "/Logs/Snapshots/" + deviceModelName.get() + "_" + className + "_" + testCaseID + ".png";
        if (isAndroid()) {
            AdbHelper.captureScreen(snapshotPath, device.get());
        } else {
            AdbHelper.iosCaptureScreen(snapshotPath, device.get());
        }
        return snapshotPath;
    }

    /**
     * Function for returning the resource map
     *
     * @param p_strDisplayLang
     * @return Resource Map.
     */
    public static Map<String, String> getResourceMap(String p_strDisplayLang) {
        // FrameworkLogger.logStep("In getResourceMap() Method to Fetch resource strings");
        Map<String, String> l_objResourceMap =
                new HashMap<String, String>();
        Locale locale = new Locale(p_strDisplayLang);
        ResourceBundle l_objResourceBundle = null;
        try {
            String l_strKey = null;
            String l_strValue = null;
            String l_strFileName = "resource";

           /* l_objResourceBundle =
                    PropertyResourceBundle.getBundle(l_strFileName,
                            locale);
            Enumeration<String> l_objKeySet =
                    l_objResourceBundle.getKeys();
            while (l_objKeySet.hasMoreElements()) {
                l_strKey = l_objKeySet.nextElement();
                l_strValue = l_objResourceBundle.getString(l_strKey);*/
                l_objResourceMap.put(l_strKey, l_strValue);


        } catch (MissingResourceException p_objIOException) {
            //FrameworkLogger.logFail("Resource file Loading error");
            p_objIOException.printStackTrace();
        }
        return l_objResourceMap;
    }

    private void initializeAppiumDriver(String deviceName, String platformVersion, String port) {
        System.out.println("******Start**********BaseTestScript***********");

        HeaderData headerData = getClass().getAnnotation(HeaderData.class);
        HeaderData.DataType dataType = headerData != null && headerData.dataType() != null ? headerData.dataType() : HeaderData.DataType.EMPTY;
        String testDataPath = headerData != null ? headerData.testDataPath() : HeaderData.EMPTY_VALUE;
        commandTimeOut = headerData != null ? headerData.commandTimeout() : HeaderData.EMPTY_VALUE;
        isResetRequired = headerData.isResetRequired();
        try {
            isDebug = Boolean.parseBoolean(Util.readProperties("isDebugMode"));
            className = this.getClass().getSimpleName();
            System.out.println("@Initializer : initData");
            initTestData(dataType, testDataPath);
            if (isDebug) {
                port = "4723";
            } else {
//                port = System.getProperty("port");
            }
            System.out.println("@Initializer : Port : " + port);
            System.out.println("@Initializer : Before Set Driver");
            setDriver(deviceName, platformVersion, port);
            Util.clearProjectProperty(deviceModelName.get() + ".Properties");

            resultMap.clear();
            System.out.println("@Initializer : Success");
        } catch (Exception e) {
            try {
                Util.setProjectProperty("Project.Properties", "isServerRestartRequired", "true");
                FrameworkLogger.logFail(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }


    /**
     * Initializes test data based on obtained data type and path to the test data
     *
     * @param dataType - a data type
     * @param path     - a path to the test data
     */
    public void initTestData(HeaderData.DataType dataType, String path) throws ParseException {
        System.out.println("******Start**********initTestData***********");

        TestDataReader dataReader = getTestDataReaderByDataType(dataType);

        boolean pathValid = path != null && !path.isEmpty() && !HeaderData.EMPTY_VALUE.equals(path);
        dataContainer = dataReader != null ? (pathValid ? dataReader.readTestData(path) : dataReader.readTestData(getClass())) : null;
    }

    /**
     * Method to select dataType
     *
     * @param dataType : type of data
     * @return : Test Data
     */
    private TestDataReader getTestDataReaderByDataType(HeaderData.DataType dataType) {
        System.out.println("******Start**********getTestDataReaderByDataType***********");

        switch (dataType) {
            case JSON:
                return new JSONDataReader();
            case XML:
                return new XMLDataReader();
            case EXCEL:
                return new ExcelDataReader();
            case CSV:
                return new CSVDataReader();
        }
        return null;
    }

    /**
     * Initialise Header data and Logger
     *
     * @param deviceName
     * @param platformVersion
     * @param portNo
     * @throws Exception : gives exception
     */
    private synchronized void  setDriver(String deviceName, String platformVersion, String portNo) throws Exception {
        //setDevice(port);

        Annotation annotation = this.getClass().getAnnotation(HeaderData.class);
        HeaderData headerData = (HeaderData) annotation;
        testCaseID = headerData.testCaseId();
        String requirementId = headerData.requirementID();
        apkFilePath = new File("./binaries/" + portNo + ".apk");
        ipaFilePath = new File("./binaries/" + portNo + ".ipa");
        String path = System.getProperty("user.dir");
        chromeDriverPath = new File("./Resources/browserexes/chromedriver.exe");

         //deviceModelName.set(deviceModelNameList.get(InitializerScript.deviceName.indexOf(device.get())).replaceAll(" ", "_"));
        deviceModelName.set(deviceModelNameList.get(InitializerScript.deviceName.indexOf(deviceName)).replaceAll(" ", "_"));

        testID = Arrays.asList(testCaseID.split(","));
        System.out.println("## classname " + this.getClass().getSimpleName() + "  Global deviceModelName =" + deviceModelName.get() + "  local deviceName =" + deviceName + " InitializerScript.deviceName= " + InitializerScript.deviceName);
        System.out.println("## local parameters " + this.getClass().getSimpleName() + "  local deviceName =" + deviceName + " platformVersion= " + platformVersion + "  port= "+portNo);
        System.out.println("## global parameters " + this.getClass().getSimpleName() + "  global deviceModelName =" + deviceModelName.get() + "global devicename :"+device.get()+ " gloabl platformVersion= " + platform.get() + " global port= "+ port.get());

        System.out.println("@Initializer : setDriver Method : Before Excel Result Helper");
        for (String test : testID) {
            ExcelResultHelper.setDeviceAndScriptName(requirementId, test.trim(),
                    this.getClass().getSimpleName(), deviceModelName.get());
        }
        FrameworkLogger.config(requirementId, testCaseID, this.getClass().getSimpleName(), deviceModelName.get(),
                headerData.testScriptDescription());
        System.out.println("The log name has been set to " + FrameworkLogger.LOGFILENAME.get());
        setAppiumDriver(deviceName, platformVersion, portNo);

        if (isIOS()) {
            driver.get().manage().timeouts().implicitlyWait(implicitTimeOut, TimeUnit.MINUTES);

        } else if (isAndroid()) {
            platform .set((String) driver.get().getCapabilities().getCapability("platformName"));
        }

    }

    /**
     * Method to set device for each port
     *
     * @param port : String port
     * @throws Exception : Throws Exception
     */
    public void setDevice(String port) throws Exception {
        switch (port) {
            case "4723":
                device.set(deviceName.get(0));
                break;
            case "4727":
                device.set(deviceName.get(1));
                break;
            case "4731":
                device.set(deviceName.get(2));
                break;
            case "4735":
                device.set(deviceName.get(3));
                break;
            case "4739":
                device.set(deviceName.get(4));
                break;
        }
    }

    /**
     * Method to set appium capabilities for Android
     *
     * @param platformVersion : device version
     * @param deviceName      : device name
     * @param port            : appium port
     * @throws Exception : Throws Exception
     */
    private void setAndroidDriver(String platformVersion, String deviceName, String port, boolean isResetRequired) throws Exception {
        System.out.println("******Start**********setAndroidDriver***********");
        // Set up desired capabilities and pass the Android app-activity and app-package to Appium
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobCapabilityType.PLATFORM_NAME.getValue(), Constants.ANDROID);
        capabilities.setCapability(MobCapabilityType.PLATFORM_VERSION.getValue(), platformVersion);
        capabilities.setCapability(MobCapabilityType.DEVICE_NAME.getValue(), deviceName);
        capabilities.setCapability(MobCapabilityType.APP_ACTIVITY.getValue(), activityName);
        capabilities.setCapability(MobCapabilityType.APP_PACKAGE.getValue(), packageName);
        // capabilities.setCapability(MobCapabilityType.AUTOMATION_NAME.getValue(), "UiAutomator2");
        capabilities.setCapability("autoGrantPermissions", "true");

        capabilities.setCapability(MobCapabilityType.NEW_COMMAND_TIMEOUT.getValue(), commandTimeOut);
        //capabilities.setCapability("unicodeKeyboard", true);
        FrameworkLogger.logStep("Default noReset is false");
        if (!isResetRequired) {
            FrameworkLogger.logStep(" capability, noReset = true, so as not to re-login again");
            //capabilities.setCapability("app", apkFilePath.getCanonicalPath());
            //capabilities.setCapability("noReset", true) : does not require to re login
            capabilities.setCapability("noReset", true);
        }
        //capabilities.setCapability("autoWebview", "true");
        //capabilities.setCapability("autoWebviewTimeout", "6000");

        /*String strLocale = Util.readFile("Project.Properties", "locale");
        switch (strLocale) {
            case "en":
                System.out.println("English language capability set");
                //capabilities.setCapability("locale", "en_US");
                //capabilities.setCapability("language", "en");

                break;
            case "hi":
                System.out.println("Hindi language capability set");
                //capabilities.setCapability("locale", "IN");
                //capabilities.setCapability("language", "hi");
                break;
            case "ma":
                System.out.println("Marathi language capability set");
                break;

            default:
                System.out.println("English language default set");
                capabilities.setCapability("locale", "en_US");
                capabilities.setCapability("language", "en");
                break;
        }*/

        driver.set(new AndroidDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities));
        androidDriver = (AndroidDriver) driver.get();
        driver.get().manage().timeouts().implicitlyWait(implicitTimeOut, TimeUnit.SECONDS);

        System.out.println("Capability set for Android  " + device.get());
    }

    /**
     * Method to set appium capabilities for IOS
     *
     * @param deviceName : device name
     * @param port       : appium port
     * @throws IOException : Throws IO Exception
     */
    private void setIOSDriver(String deviceName, String port, String bundleID, boolean isResetRequired) throws IOException {
        System.out.println("******Start**********setIOSDriver***********");

        // Set up desired capabilities and pass the IOS app-activity and app-package to Appium
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobCapabilityType.PLATFORM_NAME.getValue(), Constants.IOS);
        capabilities.setCapability(MobCapabilityType.DEVICE_NAME.getValue(), deviceName.trim());
        capabilities.setCapability(MobCapabilityType.NEW_COMMAND_TIMEOUT.getValue(), commandTimeOut);
        capabilities.setCapability("bundleId", bundleID);
        capabilities.setCapability("app", ipaFilePath.getCanonicalPath());
        capabilities.setCapability("autoLaunch", true);
        if (isResetRequired) {
            capabilities.setCapability("noReset", isResetRequired);
        }
        /*capabilities.setCapability("autoAcceptAlerts", true);*/
        capabilities.setCapability("waitForAppScript", true);
        driver.set(new IOSDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities));
        iosDriver = (IOSDriver) driver.get();
        System.out.println("Capability set for IOS");
    }

    /**
     * The method will be called inside test method if test data are absent
     *
     * @throws Exception - throws {@link NotImplementedException} if method was not overridden
     */
    protected void invokeTest() throws Exception {
        throw new NotImplementedException();
    }

    /**
     * The method will be called inside test method and will obtain concrete test data records
     *
     * @param testDataRecord a test data record
     * @param index-         an index of a test data record in the test data list
     * @see
     */
    protected void invokeDataDrivenTest(TestDataRecord testDataRecord, int index) throws Exception {
        throw new NotImplementedException();
    }


    /**
     * Method to start appium driver
     *
     * @throws Exception
     */
    protected void setAppiumDriver(String deviceName, String platformVersion, String url) throws Exception {
        System.out.println("******Start**********setDriver***********");
        switch (port.get()) {
            case "4723":
                initializeDriver(deviceName, platformVersion, url);
                break;
            case "4727":
                initializeDriver(deviceName, platformVersion, url);
                break;
            case "4731":
                initializeDriver(deviceName, platformVersion, url);
                break;
            case "4735":
                initializeDriver(deviceName, platformVersion, url);
                break;
            case "4739":
                initializeDriver(deviceName, platformVersion, url);
                break;
        }
    }

    /**
     * Method to initialize driver
     *
     * @param deviceName
     * @param platformVersion
     * @param url
     * @throws Exception
     */
    private void initializeDriver(String deviceName, String platformVersion, String url) throws Exception {
        HeaderData headerData = getClass().getAnnotation(HeaderData.class);
        boolean isResetRequired = headerData.isResetRequired();
        /*if (isResetRequired) {
            String killAppCommand = CommandsFactory.getStopCommand(device, packageName);
            Util.executeCommand(killAppCommand);
            AdbHelper.clearApplicationCache(packageName);
            Thread.sleep(3000);
            String startAppCommand = CommandsFactory.startActivity(device, packageName, activityName);
            Util.executeCommand(startAppCommand);
            Thread.sleep(3000);
        }*/
        if (deviceModelName.get().contains("iPad") || deviceModelName.get().contains("iPhone")) {
            setIOSDriver(deviceName, url, bundleID, isResetRequired);
        } else {
            setAndroidDriver(platformVersion, deviceName, url, isResetRequired);
        }
        System.out.println("@Initializer : After Initialising Driver");
    }



   /* public static class UsesErrorCollector {
        @Rule
        public ErrorCollector collector = new ErrorCollector();

        @Test
        public void example() {
            collector.addError(new Throwable("message"));
        }
    }*/

    public static String getLocale() {

        String strRetLocale = "";

        try {
            strRetLocale = Util.readFile("Project.Properties", "locale");
            if (strRetLocale == null || strRetLocale == "") {
                strRetLocale = "en";
            }
            switch (strRetLocale) {
                case "en":
                    break;
                case "english":
                    strRetLocale = "en";
                    break;
                case "hi":
                    break;
                case "hindi":
                    strRetLocale = "hi";
                    break;
                default:
                    strRetLocale = "en";

                    break;
            }

        } catch (MissingResourceException e1) {

            e1.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return strRetLocale;
    }


}
