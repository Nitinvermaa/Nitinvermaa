package com.swg.framework.api.adb;

import com.swg.framework.logging.FrameworkLogger;

/**
 * <p>
 * CommandsFactory specifies a factory for creation of adb -s %s commands
 * </p>
 */
public class CommandsFactory {

    public static String getDevicesCommand() {
        if (System.getProperty("os.name").contains("Windows")) {
            return "adb devices | findstr /E device";
        } else {
            return "adb devices | grep -w device";
        }
    }

    public static String getKillServerCommand() {
        return "adb kill-server";
    }

    public static String getStartServerCommand() {
        return "adb start-server";
    }

    public static String getBatteryCommand() {
        return "adb -s %s shell dumpsys battery | grep 'level'";
    }

    public static String getMemoryCommand(String device, final String packageName) {
        return String.format("adb -s %s shell dumpsys meminfo '%s'", device, packageName);
    }

    public static String getCPUInfoCommand(String device, final String packageName) {
        return String.format("adb -s %s shell dumpsys cpuinfo | grep '%s'", device, packageName);
    }

    public static String getCaptureScreenCommand(String device, final String tabletFilePath) {
        return String.format("adb -s %s shell screencap -p %s", device, tabletFilePath);
    }

    public static String getPullCommand(String device, final String tabletFilePath, final String testStationPath) {
        return String.format("adb -s %s pull %s %s", device, tabletFilePath, testStationPath);
    }

    public static String getPushCommand(String device, final String filePath, final String tabletPath) {
        return String.format("adb -s %s push %s %s", device, filePath, tabletPath);
    }

    public static String getRemoveFileCommand(String device, final String tabletFilePath) {
        return String.format("adb -s %s shell rm %s", device, tabletFilePath);
    }

    public static String getRebootCommand(String device) {
        return String.format("adb -s %s reboot", device);
    }

    public static String getCloseKeyboardCommand(String device) {
        return String.format("adb -s %s shell input tap 200 200", device);
    }

    public static String getClickCommand(String device, int x, int y) {
        return String.format("adb -s %s shell input tap %d %d", device, x, y);
    }

    public static String getSwipeCommand(String device, int xStart, int yStart, int xEnd, int yEnd) {
        return String.format("adb -s %s shell input swipe %d %d %d %d", device, xStart, yStart, xEnd, yEnd);
    }

    public static String getSwipeCommand(String device, int xStart, int yStart, int xEnd, int yEnd, int duration) {
        return String.format("adb -s %s shell input swipe %d %d %d %d %d", device, xStart, yStart, xEnd, yEnd, duration);
    }

    public static String getLockCommand(String device) {
        return String.format("adb -s %s shell input keyevent 26", device);
    }

    public static String getUnlockCommand(String device) {
        return String.format("adb -s %s shell input keyevent 82", device);
    }

    public static String getCallCommand(String device, String phoneNumber) {
        return String.format("adb -s %s shell am start -a android.intent.action.CALL -d tel: %s", device, phoneNumber);
    }

    public static String getAcceptCallCommand(String device) {
        return String.format("adb -s %s shell input keyevent 5", device);
    }

    public static String getEndCallCommand(String device) {
        return String.format("adb -s %s shell input keyevent 6", device);
    }

    public static String getVolumeUpCommand(String device) {
        return String.format("adb -s %s shell input keyevent 24", device);
    }

    public static String getVolumeDownCommand(String device) {
        return String.format("adb -s %s shell input keyevent 25", device);
    }

    public static String getMuteCommand(String device) {
        return String.format("adb -s %s shell input keyevent 164", device);
    }

    public static String getSleepCommand(String device) {
        return String.format("adb -s %s shell input keyevent 223", device);
    }

    public static String getWakeUpCommand(String device) {
        return String.format("adb -s %s shell input keyevent 224", device);
    }

    public static String getChangeTimeFormatCommand(String device, String timeFormat) {
        return String.format("adb -s %s shell content insert --uri content://settings/system  --bind name:s:time_12_24 " +
                "--bind value:s: %s", device, timeFormat);
    }

    public static String getContactPackageCommand(String device) {
        if (System.getProperty("os.name").contains("Windows")) {
            return String.format("adb -s %s  shell pm list packages -f | findstr android.contact", device);
        } else {
            return String.format("adb -s %s  shell pm list packages -f | grep android.contact", device);
        }
    }

    public static String getInstallApplicationCommand(String device, String pathToAppFile) {
        return String.format("adb -s %s install -r %s", device, pathToAppFile);
    }

    public static String getUninstallCommand(String device, String packageName) {
        return String.format("adb -s %s uninstall %s", device, packageName);
    }

    public static String getStartCommand(String device, String activityName) {
        return String.format("adb -s %s shell am start -n %s", device, activityName);
    }

    public static String getStopCommand(String device, String packageName) {
        return String.format("adb -s %s shell am force-stop %s", device, packageName);
    }

    public static String getIsApplicationLaunchedCommand(String device, String packageName) {
        return String.format("adb -s %s shell ps | grep %s", device, packageName);
    }

    public static String getClearApplicationData(String device, String packageName) {
        return String.format("adb -s %s shell pm clear %s", device, packageName);
    }

    public static String getDeviceInfoCommand(String device) {
        return String.format("adb -s %s shell getprop | grep -E \"ro.product.manufacturer\\|ro.product.model\\|" +
                "ro.build.version.release\\|ro.product.name\\|ro.serialno\"", device);
    }

    public static String getLogcatCommand(String device, String path) {
        return String.format("adb -s %s shell logcat -d -v time -d > %s", device, path);
    }

    public static String getDateCommand(String device) {
        return String.format("adb -s %s shell date", device);
    }

    public static String getDeleteAllContactsCommand(String device) {
        return String.format("adb -s %s shell content delete --uri content://com.android.contacts/raw_contacts/", device);
    }

    public static String getDeleteSpecificContactsCommand(String device, String contactId) {
        return String.format("adb -s %s shell content delete --uri content://com.android.contacts/raw_contacts/ " +
                "--where \"_id='%s'\"", device, contactId);
    }

    public static String getDeleteSocialContactsCommand(String device, String accountType) {
        return String.format("adb -s %s shell content delete --uri content://com.android.contacts/raw_contacts/ " +
                "--where \"account_type='%s'\"", device, accountType);
    }

    public static String getDeleteOnlyNativeContactsCommand(String device) {
        return String.format("adb -s %s shell content delete --uri content://com.android.contacts/raw_contacts/ " +
                "--where \"account_type!='com.google'\"", device);
    }

    public static String getVerifyDeletedContactCommand(String device, String contactId) {
        return String.format("adb -s %s shell content query --uri content://com.android.contacts/raw_contacts/  " +
                "--projection deleted --where \"_id='%s'\"", device, contactId);
    }

    public static String getImportContactsCommand(String device, String remotePath, String vcfFileName, String contactPackageName) {
        return String.format("adb -s %s shell am start -t \"text/x-vcard\" -d \"file://%s/%s\" " +
                "-a android.intent.action.VIEW %s", device, remotePath, vcfFileName, contactPackageName);
    }

    public static String getIsFileExitsCommand(String device, final String filePath) {
        return String.format("adb -s %s shell ls %s", device, filePath);
    }

    public static String getDeviceVersionCommand(String device) {
        return String.format("adb -s %s shell getprop ro.build.version.release", device);
    }

    public static String getAppBuildVersionCommand(String device) {
        return String.format("adb -s %s shell dumpsys package com.swaglabsmobileapp | grep versionName", device);
    }

    public static String getDeviceModelCommand(String device) {
        return String.format("adb -s %s shell getprop ro.product.model", device);
    }

    public static String getDeviceManufacturerCommand(String device) {
        return String.format("adb -s %s shell getprop ro.product.manufacturer", device);
    }

    public static String getApplicationVersionCommand(String device, String packageName) {
        return String.format("adb -s %s shell dumpsys package %s", device, packageName);
    }

    private static String getContactDataTableCommand(String device) {
        return String.format("adb -s %s shell content query --uri content://com.android.contacts/data/ --projection ",
                device);
    }

    public static String getAllContactIdsCommand(String device) {
        return getContactDataTableCommand(device) + " raw_contact_id --where \"mimetype='vnd.android.cursor.item/name'\"";
        /*return String.format("adb -s %s shell content query --uri content://com.android.contacts/raw_contacts/  " +
                "--projection _id --where \"deleted='0'\"", device);*/
    }

    public static String getContactIdCommand(String device, String projection, String firstName, String mimeType) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"data2='%s' " +
                "and mimetype='%s'\"", projection, firstName, mimeType);
    }

    public static String getContactCountCommand(String device) {
        return getContactDataTableCommand(device) + " raw_contact_id --where \"mimetype_id='7'\"";
    }

    public static String getUpdateContactCommand(String device, String newConName, String id, String mimetype) {
        return String.format("adb -s %s shell content update --uri content://com.android.contacts/data/ " +
                "--bind data2:s:%s --where \"raw_contact_id='%s' and mimetype='%s'\"", device, newConName, id, mimetype);
    }

    public static String getContactCommand1(String device, String projection, String contactId, String mimeType) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"raw_contact_id='%s' " +
                "and mimetype='%s'\"", projection, contactId, mimeType);
    }

    public static String getContactCommand2(String device, String projection, String contactId, String mimeType, String data2Column) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"raw_contact_id='%s' " +
                "and mimetype='%s' and data2='%s'\"", projection, contactId, mimeType, data2Column);
    }

    public static String getContactCommand3(String device, String projection, String contactId, String mimeType, String data5Column) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"raw_contact_id='%s' " +
                "and mimetype='%s' and data5='%s'\"", projection, contactId, mimeType, data5Column);
    }

    public static String getContactCommand4(String device, String projection, String contactId, String mimeType, String data1Column) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"raw_contact_id='%s' " +
                "and mimetype='%s' and data1='%s'\"", projection, contactId, mimeType, data1Column);
    }

    public static String getContactNameCommand(String device, String id) {
        return String.format(getContactDataTableCommand(device) + " data1 --where \"raw_contact_id='%s' " +
                "and mimetype_id='7'\"", id);
    }

    public static String getContactIdCommand(String device) {
        return getContactDataTableCommand(device) + " raw_contact_id";
    }

    public static String getContactVersionCommand(String device, String contactId) {
        return String.format("adb -s %s shell content query --uri content://com.android.contacts/raw_contacts/ " +
                "--projection version --where \"_id='%s'\"", device, contactId);
    }

    public static String getRecentAppCommand(String device) {
        return String.format("adb -s %s shell input keyevent KEYCODE_APP_SWITCH", device);
    }

    public static String getSelectRecentAppCommand(String device) {
        return String.format("adb -s %s shell input keyevent KEYCODE_DPAD_DOWN", device);
    }

    public static String getCloseRecentAppCommand(String device) {
        return String.format("adb -s %s shell input keyevent DEL", device);
    }

    public static String getCurrentActivityCommand(String device) {
        return String.format("adb -s %s shell \"dumpsys window windows | grep -E 'mCurrentFocus'\"", device);
    }

    public static String getKeyboardVisibleCommand(String device) {
        return String.format("adb -s %s shell \"dumpsys window InputMethod | grep -E 'mHasSurface'\"", device);
    }

    public static String startActivity(String device, String packageName, String activityName) {
        FrameworkLogger.logStep("Start Activity on " + device + " in it's current state");
        return String.format("adb -s " + device + " shell am start -n " + packageName + "/" + activityName);
    }

    public static String getForeStop(String device, String packageName) {
        return String.format("adb -s %s shell am force-stop %s", device, packageName);
    }

    /**
     * Method to turn OFF the gps
     */
    public static String getGPSOFF() {
        return String.format("adb shell settings put secure location_providers_allowed -gps");
    }

    /**
     * Method to turn on the gps
     */
    public static String getGPSON(String device) {
        return String.format("adb shell settings put secure location_providers_allowed +gps");
    }

    /**
     * Method to turn OFF the WiFi
     */
    public static String getWifiOff(String device) {
        return String.format("adb -s %s shell am start -n io.appium.settings/.Settings -e wifi off", device);
    }

    public static String grantPermissionsTo(String device, String packageName, String permissionName) throws Exception {
        return String.format("adb -s %s shell pm grant %s %s", device, packageName, permissionName);
    }


    public static String revokePermissionOf(String device, String packageName, String permissionName) throws Exception {
        return String.format("adb -s %s shell pm revoke %s %s", device, packageName, permissionName);
    }

    /**
     * Method to turn ON the WiFi
     */

    public static String getWifiON(String device) {
        return String.format("adb -s %s shell am start -n io.appium.settings/.Settings -e wifi on", device);
    }

    /**
     * Method to open wifi setting screen
     */

    public static String getWifiSettingScreen(String device) {
        return String.format("adb -s %s shell am start -n com.android.settings/.wifi.WifiSettings", device);
    }
    //adb shell am start -n com.android.settings/.wifi.WifiSettings

    public static String getFileDetailsFromNative(String device, String deviceFilePath) {
        return String.format("adb -s %s shell ls %s", device, deviceFilePath);
    }



    public static String getSendIntentCommand(String device, String applicationAndFileType) {
        return String.format("adb -s %s shell am start -a android.intent.action.SEND  -t %s", device, applicationAndFileType);
    }


    public static String getHitKeyboardEnter(String device) {
        return String.format("adb -s %s shell input keyevent KEYCODE_ENTER", device);
    }

    /*
    sendKeysUsingADB() : This method helps to enter text using adb command (Usually used on web view)
     */
    public static String sendKeysUsingADB(String inputString) {
        return String.format("adb shell input text \"%s\"", inputString);
    }

    public static String collapseNotificationDrawer() {
        return String.format("adb shell service call statusbar 2");
    }

}
