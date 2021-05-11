package com.swg.framework.api.adb;

import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.setup.InitializerScript;
import com.swg.framework.setup.StartAppiumServer;
import com.swg.framework.utility.Util;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for adb commands.
 */
public class AdbHelper {
    private static int counter = 0;
    private static String deviceId = InitializerScript.device.get();

    /**
     * Method to get Device Name
     *
     * @return : list of devices attached.
     * @throws Exception
     */
    public static List<String> deviceName() throws Exception {
        String osName = System.getProperty("os.name");
        List<String> devices = new ArrayList<>();
        List<String> deviceName = new ArrayList<>();

        if (osName.contains("Windows")) {
            devices = Util.getDeviceName(CommandsFactory.getDevicesCommand());
        } else {
            try {
                List<String> androidDevices = Util.iosRunProcess(false, "adb devices | grep -w 'device'");
                if (!androidDevices.isEmpty()) {
                    devices.addAll(androidDevices);
                }
                List<String> iosDevices = Util.iosRunProcess(true, "idevice_id -l");
                devices.addAll(iosDevices);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (devices != null) {
            for (String device : devices) {
                if (device.equals("List of devices attached ") || device.equals("")) {
                    deviceName.remove(device);
                } else if (device.contains(":")) {
                    deviceName.add(device.replace(":", ""));
                } else {
                    deviceName.add(device.replace("\tdevice", ""));
                }
            }
        }
        if (deviceName.isEmpty() && counter <= 2) {
            if (counter < 2) {
                Util.executeCommand(CommandsFactory.getKillServerCommand());
                Util.executeCommand(CommandsFactory.getStartServerCommand());
                counter++;
                System.out.println(counter);
                deviceName();
            } else {
                throw new Exception("Device not Found");
            }
        }
        counter = 0;
        return deviceName;
    }

    /**
     * Method to get device version
     *
     * @param deviceName : list of device name
     * @return : list of device version
     */
    public static List<String> deviceVersion(List<String> deviceName) {
        List<String> deviceVersion = new ArrayList<>();
        for (String aDeviceName : deviceName) {
            if (isWindows()) {
                String deviceId = aDeviceName.replace("\tdevice", "");
                String osVersion = String.valueOf(Util.runProcess(CommandsFactory.getDeviceVersionCommand(deviceId))).
                        replace("[", "");

                osVersion = osVersion.replace(", ]", "");
                deviceVersion.add(osVersion);
            } else {
                try {
                    String deviceId = aDeviceName.trim();
                    List<String> androidDeviceId = Util.iosRunProcess(false, CommandsFactory.getDeviceVersionCommand(deviceId));
                    if (!androidDeviceId.isEmpty()) {
                        deviceVersion.add(androidDeviceId.get(0));
                    }

                    if (deviceId.length() >= 40) {
                        String iosDeviceId = Util.iosRunProcess(true, "ideviceinfo -u " + deviceId + " |grep ProductVersion").get(0);
                        deviceVersion.add(iosDeviceId.substring(iosDeviceId.indexOf(" ")).trim());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return deviceVersion;
    }

    /**
     * Method to get Device id
     *
     * @param deviceNameList : list of device name
     * @return : list of device id
     */
    public static List<String> deviceID(List<String> deviceNameList) {
        List<String> deviceID = new ArrayList<>();
        for (String deviceName : deviceNameList) {
            deviceID.add(deviceName);
        }
        return deviceID;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    /**
     * Method to get iphone device list
     *
     * @return : iPhone Model Map
     */
    public static Map<String, String> getIPhoneModelList() {
        Map<String, String> deviceMap = new HashMap<>();
        deviceMap.put("iPhone4,1", "iPhone 4s");
        deviceMap.put("iPhone5,1", "iPhone 5");
        deviceMap.put("iPhone5,2", "iPhone 5");
        deviceMap.put("iPhone5,3", "iPhone 5c");
        deviceMap.put("iPhone5,4", "iPhone 5c");
        deviceMap.put("iPhone6,1", "iPhone 5s");
        deviceMap.put("iPhone6,2", "iPhone 5s");
        deviceMap.put("iPhone7,2", "iPhone 6");
        deviceMap.put("iPhone7,1", "iPhone 6 Plus");
        deviceMap.put("iPhone8,1", "iPhone 6s");
        deviceMap.put("iPhone8,2", "iPhone 6s Plus");
        deviceMap.put("iPad2,1", "iPad 2");
        deviceMap.put("iPad2,2", "iPad 2");
        deviceMap.put("iPad2,3", "iPad 2");
        deviceMap.put("iPad2,4", "iPad 2");
        deviceMap.put("iPad3,1", "iPad 3");
        deviceMap.put("iPad3,2", "iPad 3");
        deviceMap.put("iPad3,3", "iPad 3");
        deviceMap.put("iPad3,4", "iPad 4");
        deviceMap.put("iPad3,5", "iPad 4");
        deviceMap.put("iPad3,6", "iPad 4");
        deviceMap.put("iPad4,1", "iPad Air");
        deviceMap.put("iPad4,2", "iPad Air");
        deviceMap.put("iPad4,3", "iPad Air");
        deviceMap.put("iPad5,3", "iPad Air 2");
        deviceMap.put("iPad5,4", "iPad Air 2");
        return deviceMap;
    }

    public static String getIOSDeviceModel(String productType) {
        return getIPhoneModelList().get(productType);
    }

    /**
     * Method to get Device model name
     *
     * @param deviceNameList : list of device name
     * @return : list of device model name
     */
    public static List<String> deviceModelName(List<String> deviceNameList) {
        List<String> deviceModelName = new ArrayList<>();
        for (String aDeviceNameList : deviceNameList) {
            if (isWindows()) {
                String deviceId = aDeviceNameList.replace("\tdevice", "");
                String deviceModel = Util.getModelName(CommandsFactory.getDeviceModelCommand(deviceId));
                if (deviceModelName.contains(deviceModel)) {
                    deviceModel = deviceModel + "_" + String.valueOf(deviceNameList.indexOf(aDeviceNameList));
                    deviceModelName.add(deviceModel);
                } else {
                    deviceModelName.add(deviceModel);
                }
            } else {
                List<String> deviceModel;
                try {
                    deviceModel = Util.iosRunProcess(false, CommandsFactory.getDeviceModelCommand(aDeviceNameList));
                    if (!deviceModel.isEmpty()) {
                        if (deviceModelName.contains(deviceModel.get(0))) {
                            String device = deviceModel.get(0) + "_" + String.valueOf(deviceNameList.indexOf(aDeviceNameList));
                            deviceModelName.add(device);
                        } else {
                            deviceModelName.add(deviceModel.get(0));
                        }
                    }
                    if (aDeviceNameList.length() >= 40) {
                        String iosVersion = Util.iosRunProcess(true, "ideviceinfo -u " + aDeviceNameList +
                                " |grep ProductType").get(0);
                        String model = getIOSDeviceModel(iosVersion.substring(iosVersion.indexOf(" ")).trim());
                        if (deviceModelName.contains(model)) {
                            model = model + "_" + String.valueOf(deviceNameList.indexOf(aDeviceNameList));
                            deviceModelName.add(model);
                        } else {
                            deviceModelName.add(model);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return deviceModelName;
    }

    /**
     * Method to get Application Build Version
     *
     * @param deviceList : List of device name
     * @return : application build version
     */
    public static String getAndroidBuildVersion(List<String> deviceList) throws IOException {
        String buildVersion = "";
        for (String eachDevice : deviceList) {
            if (isWindows()) {
                String appVersionCommand = CommandsFactory.getApplicationVersionCommand(eachDevice, InitializerScript.packageName);

                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(appVersionCommand);
                List packageInfo = IOUtils.readLines(process.getInputStream(), StandardCharsets.UTF_8);

                for (Object eachInfo : packageInfo) {
                    if (eachInfo.toString().trim().contains("versionName")) {
                        buildVersion = eachInfo.toString().substring(eachInfo.toString().indexOf("=") + 1);
                        break;
                    }
                }
            } else {
                try {
                    String modelName = StartAppiumServer.deviceModelName.get(deviceList.indexOf(eachDevice));
                    if (!(modelName.contains("iPhone") || modelName.contains("iPad"))) {
                        String deviceId = eachDevice.trim();
                        List<String> build = Util.iosRunProcess(false, CommandsFactory.getAppBuildVersionCommand(deviceId));
                        if (!buildVersion.isEmpty()) {
                            buildVersion = build.get(0).substring(build.indexOf("=" + 1), build.indexOf(","));
                        }
                    } else {
                        List<String> buildNumber = Util.iosRunProcess(true, " ideviceinstaller -u " + eachDevice +
                                " -l -o xml |grep -A3 FacebookDisplayName");
                        buildVersion = buildNumber.get(3);
                        buildVersion = buildVersion.replaceAll("<string>", "").replaceAll("</string>", "").trim();

                        /*if (buildVersion.contains(",")) {
                        buildVersion = buildVersion.split(",")[1].replaceAll("\"", "").trim();
                        } else {
                        buildVersion = buildVersion.split(" ")[3].replaceAll("\"", "").trim();
                        }*/
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return buildVersion;
    }

    /**
     * Command to reboot device
     */
    public static void rebootDevice(String device) throws Exception {
        if (isWindows()) {
            Util.executeCommand(CommandsFactory.getRebootCommand(device));
        } else {
            if (InitializerScript.isAndroid()) {
                Util.iosRunProcess(false, CommandsFactory.getRebootCommand(device));
            } else {
                Util.iosRunProcess(true, "idevicediagnostics -u " + deviceId + " restart");
            }
        }
    }

    /**
     * Method to clear Application Data
     */
    public static void clearApplicationCache(String packageName) {
        System.out.println("Clear Application Data :");
        FrameworkLogger.logStep("Clearing Application Data");
        Util.executeCommand(CommandsFactory.getClearApplicationData(deviceId, packageName));
    }

    /**
     * Method to install apk using .apk
     */
    public static void installApplication(File apkFilePath) throws InterruptedException, IOException {
        Util.executeCommand(CommandsFactory.getInstallApplicationCommand(deviceId, apkFilePath.getCanonicalPath()));
        Thread.sleep(5000l);
    }

    /**
     * Method to uninstall application using package name
     */
    public static void uninstallApplication(String app_package_name) {
        Util.executeCommand(CommandsFactory.getUninstallCommand(deviceId, app_package_name));
    }

    public static void installIOSApplication(String path) throws Exception {
        String Command = "ideviceinstaller -u " + deviceId + " -i " + path;
        Util.iosRunProcess(true, Command);

    }

    public static void unInstallIOSApplication(String deviceId, String bundleID) throws Exception {
        String Command = "ideviceinstaller -u " + deviceId + " -U " + bundleID;
        Util.iosRunProcess(true, Command);
    }

    public static void captureScreen(String fileName, String aDeviceName) {
        Util.executeCommand(CommandsFactory.getCaptureScreenCommand(aDeviceName, "/sdcard/screen.png"));
        Util.executeCommand(CommandsFactory.getPullCommand(aDeviceName, "/sdcard/screen.png ", fileName));
        Util.executeCommand(CommandsFactory.getRemoveFileCommand(aDeviceName, "/sdcard/screen.png"));
    }

    public static void iosCaptureScreen(String fileName, String aDeviceName) {
        try {
            String path = System.getProperty("user.dir");
            Util.iosRunProcess(true, "idevicescreenshot -u " + aDeviceName + " " + path + "/temp/temp.tiff");
            Util.iosRunProcess(true, "sips -s format \"jpeg\" " + path + "/temp/temp.tiff --out " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void placeCall(String phoneNumber) {
        Util.executeCommand(CommandsFactory.getCallCommand(deviceId, phoneNumber));
    }

    public static void endCall() {
        Util.executeCommand(CommandsFactory.getEndCallCommand(deviceId));
    }

    public static void acceptCall() {
        Util.executeCommand(CommandsFactory.getAcceptCallCommand(deviceId));
    }

    public static void changeTimeFormat(String requiredTimeFormat) throws InterruptedException {
        Util.executeCommand(CommandsFactory.getChangeTimeFormatCommand(deviceId, requiredTimeFormat));
        Thread.sleep(20000l);
    }

    public static List<String> getContactPackageName() throws IOException {
        List<String> list;
        if (System.getProperty("os.name").contains("Windows")) {
            list = Util.runProcess(CommandsFactory.getContactPackageCommand(deviceId));
        } else {
            list = Util.iosRunProcess(false, CommandsFactory.getContactPackageCommand(deviceId));
        }
        return list;
    }

    public static List<String> getDeviceManufacturer() throws IOException {
        List<String> list;
        if (System.getProperty("os.name").contains("Windows")) {
            list = Util.runProcess(CommandsFactory.getDeviceManufacturerCommand(deviceId));
        } else {
            list = Util.iosRunProcess(false, CommandsFactory.getDeviceManufacturerCommand(deviceId));
        }
        return list;
    }


    public static void volumeUp() {
        Util.executeCommand(CommandsFactory.getVolumeUpCommand(deviceId));
    }

    public static void volumeDown() {
        Util.executeCommand(CommandsFactory.getVolumeDownCommand(deviceId));
    }

    public static void mute() {
        Util.executeCommand(CommandsFactory.getMuteCommand(deviceId));
    }

    public static void wakeUp() {
        Util.executeCommand(CommandsFactory.getWakeUpCommand(deviceId));
    }

    public static void lock() {
        Util.executeCommand(CommandsFactory.getLockCommand(deviceId));
    }

    public static void unlock() {
        Util.executeCommand(CommandsFactory.getUnlockCommand(deviceId));
    }

    public static void sleep() throws Exception {
        if (isWindows()) {
            Util.executeCommand(CommandsFactory.getSleepCommand(deviceId));
        } else {
            if (InitializerScript.isAndroid()) {
                Util.iosRunProcess(false, CommandsFactory.getSleepCommand(deviceId));
            } else {
                Util.iosRunProcess(true, "idevicediagnostics -u " + InitializerScript.device + " sleep");
            }
        }
    }

    public static List<String> getDeviceDisplayName(List<String> deviceID) throws IOException {
        List<String> list = new ArrayList<>();
        for (String dev : deviceID) {
            if (!isWindows()) {
                if (dev.length() >= 40) {
                    String name = (Util.iosRunProcess(true, "ideviceName -u " + dev).get(0));
                    list.add(name);
                }
            }
        }
        return list;
    }

    public static void closeAllRecentApp() throws IOException {
        FrameworkLogger.logStep("Close All Recent App");
        int counter = 0;
        Util.executeCommand(CommandsFactory.getRecentAppCommand(deviceId));
        Util.executeCommand(CommandsFactory.getSelectRecentAppCommand(deviceId));
        Util.executeCommand(CommandsFactory.getCloseRecentAppCommand(deviceId));

        //check if still recent app is open.
        String currentActivity = "";
        List<String> currentActivityList = Util.runProcess_IOUtils("mCurrentFocus",
                CommandsFactory.getCurrentActivityCommand(deviceId));

        if (currentActivityList != null) {
            currentActivity = currentActivityList.get(0);
        } else {
            System.out.println("No Activity Found");
        }
        while ((currentActivity.contains("RecentsActivity") || currentActivity.contains("recents"))
                && counter <= 10) {
            System.out.println("@Adb Helper : Counter - " + counter);
            Util.executeCommand(CommandsFactory.getSelectRecentAppCommand(deviceId));
            Util.executeCommand(CommandsFactory.getCloseRecentAppCommand(deviceId));

            currentActivityList = Util.runProcess_IOUtils("mCurrentFocus",
                    CommandsFactory.getCurrentActivityCommand(deviceId));
            if (currentActivityList != null) {
                currentActivity = currentActivityList.get(0);
            } else {
                System.out.println("No Activity Found");
            }
            counter++;
        }
    }

    public static String getKeyBoardStatus() {
        List<String> test = Util.runProcess(CommandsFactory.getKeyboardVisibleCommand(InitializerScript.device.get()));
        String flag = test.get(0).substring(test.get(0).length() - 4, test.get(0).length());
        return flag;
    }

    /**
     * Method to tap record
     */
    public static void doubleTap(int x, int y) {
        Util.executeCommandForUpload(CommandsFactory.getClickCommand(deviceId, x, y));
    }

    public static final String[] STANDARD_WIRE_PERMISSIONS = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_CONTACTS",
            "android.permission.RECORD_AUDIO",
            "android.permission.CAMERA",
            "android.permission.READ_PHONE_STATE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.SEND_SMS"
    };

    public static final String[] CAM_STORAGE_AUDIO_PERMISSIONS = new String[]{
            "android.permission.RECORD_AUDIO",
            "android.permission.CAMERA"

    };

}
