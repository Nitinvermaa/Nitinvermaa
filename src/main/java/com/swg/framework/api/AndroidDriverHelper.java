package com.swg.framework.api;

import com.swg.framework.setup.InitializerScript;
import com.swg.framework.utility.Util;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Android driver commands class.
 */
public class AndroidDriverHelper {

    AndroidDriver androidDriver;

    /**
     * Constructor
     */
    public AndroidDriverHelper() {
        this.androidDriver = InitializerScript.getAndroidDriver();
    }


    /**
     * Send a key event to the device
     *
     * @param key code for the key pressed on the device
     */
    /*public void sendKeyEvent(AndroidSendKey key) {
        androidDriver.pressKeyCode(key.getValue());
    }*/

    //TODO: Implement sendkey event for different operation

    /**
     * Get the current network settings of the device.
     *
     * @return NetworkConnectionSetting objects will let you inspect the status of AirplaneMode, Wifi, Data connections
     */
    /*public NetworkConnectionSetting getNetworkConnection() {
        return androidDriver.getNetworkConnection();
    }*/

    /**
     * Set the network connection of the device. This is an Android-only method
     *
     * @param connection The NetworkConnectionSetting configuration to use for the device
     */
    /*public void setNetworkConnection(NetworkConnectionSetting connection) {
        androidDriver.setNetworkConnection(connection);
    }*/

    // TODO: Implement methods to set different network connection.

    /**
     * Save base64 encoded data as a file on the remote mobile device.
     *
     * @param remotePath Path to file to write data to on remote device
     * @param base64Data Base64 encoded byte array of data to write to remote device
     */
    public void pushFile(String remotePath, byte[] base64Data) {
        androidDriver.pushFile(remotePath, base64Data);
    }

    /**
     * This method should start arbitrary activity during a test. If the activity belongs to another application,
     * that application is started and the activity is opened.
     *
     * @param appPackage      The package containing the activity. [Required]
     * @param appActivity     The activity to start. [Required]
     * @param appWaitPackage  Automation will begin after this package starts. [Optional]
     * @param appWaitActivity Automation will begin after this activity starts. [Optional]
     * @throws IllegalArgumentException
     */
    /*public void startActivity(String appPackage, String appActivity, String appWaitPackage, String appWaitActivity)
            throws IllegalArgumentException {
        androidDriver.startActivity(appPackage, appActivity, appWaitPackage, appWaitActivity);
    }*/

    /**
     * This method should start arbitrary activity during a test. If the activity belongs to another application,
     * that application is started and the activity is opened.
     *
     * @param appPackage  The package containing the activity. [Required]
     * @param appActivity The activity to start. [Required]
     * @throws IllegalArgumentException
     */
    /*public void startActivity(String appPackage, String appActivity) throws IllegalArgumentException {
        androidDriver.startActivity(appPackage, appActivity);
    }*/

    /**
     * Get test-coverage data
     *
     * @param intent intent to broadcast
     * @param path   path to .ec file
     */
    public void endTestCoverage(String intent, String path) {
        //TODO: need to know the usages/ working of this method from appium site
        androidDriver.endTestCoverage(intent, path);
    }

    /**
     * Get the current activity being run on the mobile device
     *
     * @return current activity
     */
    public String getCurrentActivity() {
        return androidDriver.currentActivity();
    }

    /**
     * Open the notification shade, on Android devices.
     */
    public void openNotifications() {
        androidDriver.openNotifications();
    }

    /**
     * Check if the device is locked.
     *
     * @return true if device is locked. False otherwise
     */
    /*public boolean isLocked() {
        return androidDriver.isLocked();
    }*/

    public void ignoreUnimportantViews(Boolean compress) {
        androidDriver.ignoreUnimportantViews(compress);
    }

    public WebElement findElementByAndroidUIAutomator(String using) {
        //TODO: need to know the usages/ working of this method from appium site
        return androidDriver.findElementByAndroidUIAutomator(using);
    }

    public List<WebElement> findElementsByAndroidUIAutomator(String using) {
        return androidDriver.findElementsByAndroidUIAutomator(using);
    }

   /* public void enableAirplaneMode() {
        FrameworkLogger.logStep("Turn on airplane mode of device");
        NetworkConnectionSetting connection = new NetworkConnectionSetting(true, false, false);
        BaseTestScript.getAndroidDriver().setNetworkConnection(connection);
    }

    public void disableAirplaneMode() {
        FrameworkLogger.logStep("Turn Off airplane mode of device");
        NetworkConnectionSetting connection = new NetworkConnectionSetting(false, true, false);
        BaseTestScript.getAndroidDriver().setNetworkConnection(connection);
    }

    public void enableWifiMode() {
        FrameworkLogger.logStep("Turn On Wifi mode of device");
        NetworkConnectionSetting connection = new NetworkConnectionSetting(false, true, true);
        BaseTestScript.getAndroidDriver().setNetworkConnection(connection);
    }

    public void disableWifiMode() {
        FrameworkLogger.logStep("Turn Off Wifi mode of device");
        NetworkConnectionSetting connection = new NetworkConnectionSetting(false, false, false);
        BaseTestScript.getAndroidDriver().setNetworkConnection(connection);
    }

    public void enableDataMode() {
        FrameworkLogger.logStep("Turn On Data mode of device");
        NetworkConnectionSetting connection = new NetworkConnectionSetting(false, false, true);
        BaseTestScript.getAndroidDriver().setNetworkConnection(connection);
    }

    public void disableDataMode() {
        FrameworkLogger.logStep("Turn Off Data mode of device");
        NetworkConnectionSetting connection = new NetworkConnectionSetting(false, false, false);
        BaseTestScript.getAndroidDriver().setNetworkConnection(connection);
    }

    public void enableNetwork(boolean isDataDisabled, boolean isWifiDisabled) {
        if (isDataDisabled) {
            enableDataMode();
        } else if (isWifiDisabled) {
            enableWifiMode();
        }
    }*/

    /**
     * method to clear cache
     */
    public static void clearCache() {
        Util.runProcess("adb -s " + InitializerScript.device.get() + " shell pm clear com.swaglabsmobileapp");
    }

    /**
     * method to clear cache
     */
    public void installApp(String path) {
        Util.runProcess("adb -s " + InitializerScript.device + " install -r " + path);

    }

    /**
     * method to clear cache
     */
    public static void startFileManager() {
        Util.runProcess("adb -s " + InitializerScript.device + " shell am start -n com.topnet999.android.filemanager/.FileManagerActivity");
    }

    /**
     * method to kill application
     */
    public static void killApp() {
        Util.runProcess("adb -s " + InitializerScript.device.get() + " shell am force-stop com.swaglabsmobileapp");
    }


}
