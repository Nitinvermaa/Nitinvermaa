package com.swg.framework.setup;

import com.swg.framework.api.adb.AdbHelper;
import com.swg.framework.api.adb.CommandsFactory;
import com.swg.framework.execution.ExcelResultHelper;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.utility.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Start appium server for ios and android
 */
public class StartAppiumServer {
    public static String[] port = {"4723", "4727", "4731", "4735", "4739"};
    public static List<String> deviceName, deviceModelName, deviceId, osVersion;
    public static File apkFilePath, ipaFilePath;
    public static final File file = new File("Resources\\vcf ");
    static String BundleID = "com.swaglabsmobileapp", osName = System.getProperty("os.name");

  /*  public static void main(String args[]) throws Exception {
        StartAppiumServer startAppiumServer = new StartAppiumServer();
        startAppiumServer.launchServer();

    }*/

    public void launchServer(){
        String isResultUpdatedInExcel = Util.readFile("DeviceInfo.Properties", "isResultUpdated");
        try {
            if (Util.readProperties("isServerRestartRequired").equals("true")) {
                initDevices();

                String deviceBrandName = getDeviceNameAndPort(deviceModelName, port);
                Util.writeInDevicePropertiesFile(deviceBrandName + "\nnumberOfDevices=" + String.valueOf(deviceName.size()));
                //Condition for ANDROID
                if (osName.contains("Windows")) {
                    unInstallApplication();
                    installApplicationAndPushVCFFiles();
                } else if (deviceName.contains("iPhone") || deviceName.contains("iPad")) {
                    unInstallIOSApplication();
                    installIOSApplication();

                } else if (!deviceName.contains("iPhone") || !deviceName.contains("iPad")) {
                    unInstallApplication();
                    installApplicationAndPushVCFFiles();

                }

                startServer();
                Util.setProjectProperty("Project.Properties", "isServerRestartRequired", "false");
                ExcelResultHelper.setSuiteHeader();

                if (isResultUpdatedInExcel != null && isResultUpdatedInExcel.equalsIgnoreCase("false")) {
                    ExcelResultHelper.updateTestCasesResultInExcel();
                }
            } else {
                if (isResultUpdatedInExcel != null && isResultUpdatedInExcel.equalsIgnoreCase("false")) {
                    ExcelResultHelper.updateTestCasesResultInExcel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Init method to get devices
     *
     * @throws Exception
     */
    public static void initDevices() throws Exception {
        if (osName.contains("Windows")) {
            killADBServer();
            Util.executeCommand(CommandsFactory.getStartServerCommand());
        }else{
            Util.executeCommand("killall node");
            Util.executeCommand("killall Terminal");
        }
        deviceName = AdbHelper.deviceName();
        deviceModelName = AdbHelper.deviceModelName(deviceName);
        deviceId = AdbHelper.deviceID(deviceName);
        osVersion = AdbHelper.deviceVersion(deviceName);
        String path = System.getProperty("user.dir");
        ipaFilePath = new File(path + "/binaries/");
        apkFilePath = new File(path + "/binaries/");

    }


    private static boolean isPortavailable(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            System.out.println("Port is not available:: " + port);
            return false;
        } catch (IOException ignored) {
            System.out.println("Port is available:: " + port);
            return true;
        }
    }

    /**
     * Start appium server
     *
     * @throws Exception
     */
    public static void startServer() throws Exception {
        if (deviceName.size() != 0) {
            for (int i = 0; i < deviceName.size(); i++) {
                int bootStrap = Integer.parseInt(port[i]) + 1;
                int chromeDriver = Integer.parseInt(port[i]) + 2;
                writeInAppiumBatchFile(port[i], deviceId.get(i), bootStrap, chromeDriver);
                if (osName.contains("Windows")) {
                    for (int j = 1; j <= 2; j++) {
                        String dir = System.getProperty("user.dir");
                        File file = new File(dir + "/appium.bat");
                        if (file.exists()) {
                            System.out.println("Appium Batch file created successfully");
                            break;
                        } else {
                            writeInAppiumBatchFile(port[i], deviceId.get(i), bootStrap, chromeDriver);
                            System.out.println("File not exist");
                            Thread.sleep(5000l);
                        }
                    }
                    /*if (isPortavailable(Integer.parseInt(port[i]))){*/
                    Runtime.getRuntime().exec("cmd /c start appium.bat");
                    Thread.sleep(5000);
                    //}
                } else {
                    File shellScript = new File("appium.sh");
                    /* if (isPortavailable(Integer.parseInt(port[i]))) {*/
                    Runtime.getRuntime().exec("chmod u+x " + shellScript.getAbsolutePath());
                    Runtime.getRuntime().exec("/usr/bin/open -n -F -a /Applications/Utilities/Terminal.app --args " +
                            shellScript.getAbsolutePath());
                    Thread.sleep(5000);
                    //}
                }
            }
        } else {
            throw new Exception("Device not found");
        }
        waitTillAllServerIsLaunched();
    }

    /**
     * Method Create and Write commands in bash or shell file
     *
     * @param port      : Port used in appium
     * @param deviceID  : Device name
     * @param bootStrap :
     */
    public static void writeInAppiumBatchFile(String port, String deviceID, int bootStrap, int chromeDriver) {
        try {
            File file;
            if (osName.contains("Windows")) {
                file = new File("appium.bat");
                if (file.exists()) {
                    if (!file.delete()) {
                        FrameworkLogger.logFail("Appium.bat file not deleted");
                    }
                }
                if (!file.createNewFile()) {
                    FrameworkLogger.logFail("Appium.bat file not created");
                }

                port = "cd\\ \nappium -U " + deviceID + " -p " + port + " --bootstrap-port " + bootStrap +
                        " --chromedriver-port " + chromeDriver;
            } else {
                file = new File("appium.sh");
                if (file.exists()) {
                    if (!file.delete()) {
                        FrameworkLogger.logFail("Appium.sh file not deleted");
                    }
                }
                if (!file.createNewFile()) {
                    FrameworkLogger.logFail("Appium.sh file not created");
                }
                if (deviceID.length() >= 40) {
                    port = "/usr/local/bin/node /usr/local/lib/node_modules/appium/build/lib/main.js" +
                            " --address 127.0.0.1 -U " + deviceID +
                            " --port " + port + " --chromedriver-port " + chromeDriver + " --bootstrap-port " + bootStrap +
                            " --session-override --tmp /tmp/" + deviceModelName.get
                            (deviceId.indexOf(deviceID)).replaceAll(" ", "") + deviceId.indexOf(deviceID) + "/";
                } else {
                    port = "/usr/local/bin/node /usr/local/lib/node_modules/appium/build/lib/main.js" +
                            " --address 127.0.0.1 -U " + deviceID + " --port " + port + " --chromedriver-port "
                            + chromeDriver + " --bootstrap-port " + bootStrap;
                }

            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(port);
            bw.flush();
            bw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to get device model name and port number
     *
     * @param deviceModelName : List of devices
     * @param port            : string array port numbers
     * @return : device information with port number
     */
    public static String getDeviceNameAndPort(List<String> deviceModelName, String[] port) {
        String portNumber = "";
        String name = "";
        String model = "";
        String version = "";
        String id = "";
        String deliminator = ",";

        for (int i = 0; i < 5; i++) {
            if (i < deviceName.size()) {
                portNumber += "Port" + (i + 1) + "=" + port[i] + "\n" + "DeviceName" + (i + 1) + "=" + deviceModelName.get(i) + "\n";
            } else {
                portNumber += "Port" + (i + 1) + "=" + "\n" + "DeviceName" + (i + 1) + "=" + "\n";
            }

        }
        for (String device : deviceName) {
            if (device.equals(deviceName.get(deviceName.size() - 1))) {
                device = device.replace("^\\s+", "").trim();
                name += device;
            } else {
                name += device + deliminator;
            }
        }
        for (int i = 0; i < deviceModelName.size(); i++) {
            if (i == deviceModelName.size() - 1) {
                model += deviceModelName.get(i);
            } else {
                model += deviceModelName.get(i) + deliminator;
            }
        }
        for (String device : deviceId) {
            if (device.equals(deviceId.get(deviceId.size() - 1))) {
                id += device;
            } else {
                id += device + deliminator;
            }
        }
        for (int i = 0; i < osVersion.size(); i++) {
            if (i == osVersion.size() - 1) {
                version += osVersion.get(i);
            } else {
                version += osVersion.get(i) + deliminator;
            }
        }
        return portNumber + "\ndevName=" + name + "\ndeviceModel=" + model + "\ndeviceId=" + id + "\nosVersion=" + version;
    }


    /**
     * Method to install application on connected devices.
     *
     * @throws IOException
     */
    private static void installApplicationAndPushVCFFiles() throws IOException, InterruptedException {
        if (!Util.readFile("Project.Properties", "isInstalled").equals("true")) {
            for (int i = 0; i < deviceName.size(); i++) {
                System.out.println("Install APK in " + deviceModelName.get(i));
                File apkFilePath = new File("./binaries");
                Util.executeCommand(CommandsFactory.getInstallApplicationCommand(deviceName.get(i),
                        apkFilePath.getCanonicalPath()));
                Thread.sleep(5000l);
//                System.out.println("Remove ALL VCF Files");
//                Util.executeCommand(CommandsFactory.getRemoveFileCommand(deviceName.get(i),
//                        "shell rm storage/sdcard0/Download/*.vcf"));
//                Thread.sleep(5000l);
//                System.out.println("Push ALL VCF Files");
//                String pushCommand = CommandsFactory.getPushCommand(deviceName.get(i), file.getCanonicalPath(),
//                        "/storage/sdcard0/Download");
//                Util.executeCommand(pushCommand);
//                checkVCFFileExist(deviceName.get(i));
            }
            Util.setProjectProperty("Project.Properties", "isInstalled", "true");
        }
    }

    /**
     * Method to install application on connected devices.
     *
     * @throws IOException
     */
    private static void deleteAllNativeContact() throws Exception {
        deviceName = Arrays.asList(Util.readFile("DeviceInfo.Properties", "devName").split(","));
        if (deviceName.size() > 0) {
            for (String aDeviceName : deviceName) {
                Util.executeCommand(CommandsFactory.getDeleteAllContactsCommand(aDeviceName));
                Thread.sleep(5000l);
            }
        } else {
            throw new Exception("No Device Found");
        }
        killADBServer();
    }

    private static void killADBServer() throws InterruptedException {
        Util.executeCommand(CommandsFactory.getKillServerCommand());
        Thread.sleep(5000l);
    }

    /**
     * Method to uninstall application on connected devices.
     */
    private static void unInstallApplication() throws InterruptedException {
        if (!Util.readFile("Project.Properties", "isInstalled").equals("true")) {
            for (String aDeviceName : deviceName) {
                Util.executeCommand(CommandsFactory.getUninstallCommand(aDeviceName, InitializerScript.packageName));
            }
        }
        Thread.sleep(5000l);
    }

    public static void checkVCFFileExist(String deviceName) throws IOException {
        /*  String checkVCFCommand = CommandsFactory.getIsFileExitsCommand(deviceName, "storage/sdcard0/Download*//*.vcf");

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(checkVCFCommand);
        String result = IOUtils.toString(process.getInputStream());
        List vcfList = Arrays.asList(result.replaceAll("storage/sdcard0/Download/", "").
                replaceAll("\r\r", "").split("\n"));

        for (VCFFile vcf : VCFFile.values()) {
            if (!vcfList.contains(vcf.getValue())) {
                System.out.println(" -- VCF Missing - PUSH AGAIN !!" + vcf.getValue());
                String pushCommand = CommandsFactory.getPushCommand(deviceName, file.getCanonicalPath()+"\\" + vcf.getValue(),
                        " /storage/sdcard0/Download\"");

                Util.executeCommand(pushCommand);
            }
        }
        System.out.println("Validation complete");*/
    }

    private static void installIOSApplication() throws Exception {
        if (!Util.readProperties("isInstalled").equals("true")) {
            for (int i = 0; i < deviceName.size(); i++) {
                if (deviceName.get(i).length() >= 40) {
                    String Command = "ideviceinstaller -u " + deviceId.get(i) + " -i " + ipaFilePath.getAbsolutePath();
                    Util.iosRunProcess(true, Command);
                }
            }
        }
        Util.setProjectProperty("Project.Properties", "isInstalled", "true");
    }

    private static void unInstallIOSApplication() throws Exception {
        if (!Util.readProperties("isInstalled").equals("true")) {
            for (int i = 0; i < deviceName.size(); i++) {
                if (deviceName.get(i).length() >= 40) {
                    String Cmd = "ideviceinstaller -u " + deviceId.get(i) + " -U " + BundleID;
                    Util.iosRunProcess(true, Cmd);
                }
            }
        }
    }

    private static void waitTillAllServerIsLaunched() throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis();
        List<String> list;
        String command;

        while (true) {
            Thread.sleep(3000l);
            String port1 = port[deviceName.size() - 1];

            if (osName.contains("Windows")) {
                command = "netstat -anp tcp | findstr " + port1;
                list = Util.runProcess(command);
            } else {
                command = "netstat -anp tcp | grep " + port1;
                list = Util.iosRunProcess(true, command);
            }

            if (list != null && list.size() > 0) {
                System.out.println(list.get(0));
                if (list.get(0).contains("LISTENING") || list.get(0).contains("LISTEN")
                        || (System.currentTimeMillis() - startTime) > 25000) {
                    System.out.println("Server wait Time" + (System.currentTimeMillis() - startTime));
                    break;
                }
            }
        }
    }
}