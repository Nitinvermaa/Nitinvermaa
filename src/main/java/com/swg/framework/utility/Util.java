package com.swg.framework.utility;

import com.swg.framework.logging.FrameworkLogger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.openqa.selenium.logging.LogEntry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class
 */
public class Util {

    private static String response;

    private static final String[] WIN_RUNTIME = {"cmd.exe", "/C"};
    public static String[] str = {"Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"};

    /**
     * Method to create random string
     *
     * @param length : Length of string to generated
     * @return : random string
     */
    public static String randomString(int length) {
        char[] characterSet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
                '2', '3', '4', '5', '6', '7', '8', '9'};

        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);

    }

    /**
     * To format date for reporting purpose
     *
     * @return :
     */
    public static String getDate() {
        String currentDate = "";
        int t = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        currentDate = currentDate.concat(String.valueOf(t));
        t = Calendar.getInstance().get(Calendar.MONTH);
        currentDate = currentDate.concat("_" + String.valueOf(t + 1));
        t = Calendar.getInstance().get(Calendar.YEAR);
        currentDate = currentDate.concat("_" + String.valueOf(t));
        t = Calendar.getInstance().get(Calendar.HOUR);
        currentDate = currentDate.concat("-" + String.valueOf(t));
        t = Calendar.getInstance().get(Calendar.MINUTE);
        currentDate = currentDate.concat("." + String.valueOf(t));
        t = Calendar.getInstance().get(Calendar.AM_PM);

        if (t == 0) {
            currentDate = currentDate.concat("AM");
        } else {
            currentDate = currentDate.concat("PM");
        }
        return currentDate;
    }

    /**
     * Method to get Day
     *
     * @return : current day
     */
    public static String getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String getCurrentMonth() {
        String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};

        Calendar cal = Calendar.getInstance();
        return monthName[cal.get(Calendar.DAY_OF_MONTH)];
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * Write data in file
     *
     * @param path : File path
     * @param data : value which need to write in file.
     */
    public static void writeInFile(String path, String data) {
        try {
            File file = new File(path + "\\property.txt");
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    FrameworkLogger.logFail("File not created");
                }
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();

        } catch (Exception ex) {
            FrameworkLogger.logError(ex.getMessage());
        }
    }

    /**
     * Write data in device properties file.
     *
     * @param data : Value to write in file.
     */
    public static void writeInDevicePropertiesFile(String data) {
        try {
            File file = new File("DeviceInfo.Properties");
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    FrameworkLogger.logFail("DeviceInfo.Properties file not created");
                }
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        } catch (Exception ex) {
            FrameworkLogger.logError(ex.getMessage());
        }
    }

    /**
     * Method to write data in report properties file.
     *
     * @param data : Value to write in file.
     */
    public static void writeInReportPropertiesFile(String data) {
        try {
            File file = new File("Report.Properties");
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    FrameworkLogger.logFail("Report.Properties file not created");
                }
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        } catch (Exception ex) {
            FrameworkLogger.logError(ex.getMessage());
        }
    }

    /**
     * Method to convert string to hash map
     *
     * @param data :
     * @return : Map
     */
    public static HashMap<String, String> stringToHashMap(String data) {
        HashMap<String, String> mapRegion = new HashMap<>();

        data = data.replace('{', ' ');
        data = data.replaceAll("}", "").trim();

        String mapArray[] = data.split(", ");
        for (String aMapArray : mapArray) {
            String key;
            String value;
            int indexOfEqual = aMapArray.indexOf("=");
            value = aMapArray.substring(indexOfEqual + 1);
            key = aMapArray.substring(0, indexOfEqual);
            mapRegion.put(key, value);
        }
        return mapRegion;
    }

    /**
     * Used to read value for a particular key from property file.
     *
     * @param key - key to value in property file.
     * @return - value for a key from property file.
     * @throws Exception - FileNotFound and NullPointer Exception.
     */
    public static String readProperties(String key) throws Exception {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("Project.Properties"));
            return prop.getProperty(key);
        } catch (Exception ex) {
            FrameworkLogger.logError(ex.getMessage());
        }
        return "";
    }

    public static String readWebProperties(String key) throws Exception {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("Web.Properties"));
            return prop.getProperty(key);
        } catch (Exception ex) {
            FrameworkLogger.logError(ex.getMessage());
        }
        return "";
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * Method get results of unix/ios commands in a list
     *
     * @param iosProcess : set boolean value
     * @param command    : command to be executed
     * @return : List of string
     * @throws IOException
     */
    public static List<String> iosRunProcess(boolean iosProcess, String... command) throws IOException {
        long startTime = System.currentTimeMillis();
        Process p = null;
        try {
            String[] array;
            if (iosProcess) {
                array = new String[]{"/bin/sh", "-c"};
                array = concat(array, command);
            } else {
                array = new String[]{"bash", "-c"};
                array = concat(array, command);
            }
            ProcessBuilder pb = new ProcessBuilder(array);
            p = pb.start();
            p.waitFor();
        } catch (InterruptedException e) {
            FrameworkLogger.logError(e.getMessage());
        }
        InputStream inputStream = p.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String _temp;
        List<String> line = new ArrayList<>();
        while ((_temp = in.readLine()) != null) {
            if ((System.currentTimeMillis() - startTime) > 180000l) {
                FrameworkLogger.logFail("Command TimeOut : " + command[0]);
                break;
            }
            line.add(_temp);
        }
        return line;
    }

    /**
     * Method to get results of windows commands in a list
     */
    public synchronized static List<String> runProcess(String... command) {
        System.out.print("command to run: " + command[0] + " ");
        System.out.print("\n");
        long startTime = System.currentTimeMillis();
        String[] allCommand;
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"/bin/sh", "-c"}, command);
            }
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(allCommand);
            process.waitFor();
            InputStream inputStream = process.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            String _temp;
            List<String> line = new ArrayList<>();
            if (in.ready()) {
                while ((_temp = in.readLine()) != null) {
                    if ((System.currentTimeMillis() - startTime) > 180000l) {
                        FrameworkLogger.logFail("Command TimeOut : " + command[0]);
                        break;
                    }
                    line.add(_temp);
                }
            }
            return line;
        } catch (Exception e) {
            FrameworkLogger.logError(e.getMessage());
            return null;
        }
    }

    /**
     * Method to execute command
     */
    public synchronized static void executeCommand(String... command) {
        System.out.print("Execute command : " + command[0] + " ");
        System.out.print("\n");
        String[] allCommand;
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"bash", "-c"}, command);
            }
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(allCommand);
            process.waitFor();
        } catch (Exception e) {
            FrameworkLogger.logError(e.getMessage());
        }
    }

    /**
     * Method to execute command
     */
    public synchronized static void executeCommandForUpload(String... command) {
        System.out.print("Execute command : " + command[0] + " ");
        System.out.print("\n");
        String[] allCommand;
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"bash", "-c"}, command);
            }
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(allCommand);
        } catch (Exception e) {
            FrameworkLogger.logError(e.getMessage());
        }
    }


    /**
     * Read property files
     *
     * @param filename : File Name
     * @param property : property
     * @return :
     */
    public static String readFile(String filename, String property) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(filename);
            // load a properties file
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop.getProperty(property);
    }

    /**
     * Read property files
     *
     * @param filename : File Name
     * @return :
     */
    public static Map<String, String> getPropertyMap(String filename) {
        Properties prop = new Properties();
        InputStream input = null;
        Map<String, String> propertyMap = new HashMap<>();
        try {
            input = new FileInputStream(filename);
            // load a properties file
            prop.load(input);
            Set<String> property = prop.stringPropertyNames();
            for (String key : property) {
                propertyMap.put(key, prop.getProperty(key));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return propertyMap;
    }


    /**
     * Method to get suite class list
     *
     * @param filename :
     * @param property :
     * @return : Class List
     */
    public static Class[] getSuiteClasses(String filename, String property) {

        String listing[] = Util.readFile(filename, property).split(",");
        Class classList[] = new Class[listing.length];

        try {
            int i = 0;
            for (String className : listing) {
                classList[i] = Class.forName(className);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    public static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        Util.response = response;
    }

    /*
     * Getting list of class files.
     */
    public static ArrayList getClassFiles(String directory) {
        File dir = new File(directory);
        ArrayList<String> fileNames = new ArrayList<>();
        try {
            List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            for (File file : files) {
                if (System.getProperty("os.name").contains("Windows")) {
                    if (!file.toString().contains("$") && file.toString().contains("testScripts")) {
                        String fileName = file.toString().substring(file.toString().lastIndexOf("com\\")).
                                replace(".class", "").replace("\\", ".");
                        fileNames.add(fileName);
                    }
                } else {
                    if (!file.toString().contains("$") && file.toString().contains("testScripts")) {
                        String fileName = file.toString().substring(file.toString().lastIndexOf("com/")).
                                replace(".class", "").replace("/", ".");
                        fileNames.add(fileName);
                    }
                }
            }
        } catch (Exception e) {
            FrameworkLogger.logError(e.getMessage());
        }
        return fileNames;
    }

    /**
     * Set property
     *
     * @param filename :
     * @param property :
     * @param value    :
     * @throws IOException
     */
    public static void setProjectProperty(String filename, String property, String value) throws IOException {
        File propertyFile = new File(filename);
        if (!propertyFile.exists()) {
            if (!propertyFile.createNewFile()) {
                FrameworkLogger.logFail(filename + " file not created");
            }
        }
        Properties pop = new Properties();
        pop.load(new FileInputStream(propertyFile));
        pop.put(property, value);
        FileOutputStream output = new FileOutputStream(propertyFile);
        pop.store(output, "This is overwrite file");

    }

    /**
     * Set property
     *
     * @param filename :
     * @throws IOException
     */
    public static void clearProjectProperty(String filename) throws IOException {
        File propFile = new File(filename);
        if (propFile.exists()) {
            Properties pop = new Properties();
            pop.load(new FileInputStream(propFile));
            pop.clear();
            FileOutputStream output = new FileOutputStream(propFile);
            pop.store(output, "This is overwrite file");
        }
    }

    /**
     * Method to get results of windows commands in a list
     */
    public static List<String> getDeviceName(String... command) {

        System.out.println("Command::----" + command[0] + "\n");
        System.out.print("command to run:: ");
        String[] allCommand;
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"/bin/sh", "-c"}, command);
            }

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(allCommand);
            Thread.sleep(2000);

            //List deviceList = IOUtils.readLines(process.getInputStream());
            List deviceList = IOUtils.readLines(process.getInputStream(), StandardCharsets.UTF_8);


            List<String> line = new ArrayList<>();
            for (Object eachDevice : deviceList) {
                eachDevice = eachDevice.toString().substring(0, eachDevice.toString().indexOf("\t"));
                line.add(eachDevice.toString());
            }
            return line;
        } catch (Exception e) {
            FrameworkLogger.logError(e.getMessage());
            return null;
        }
    }

    public static String getModelName(String... command) {
        System.out.println("Command to Get Model Name::----" + command[0] + "\n");
        String[] allCommand;
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"/bin/sh", "-c"}, command);
            }

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(allCommand);
            Thread.sleep(2000);
            //String theString = IOUtils.toString(process.getInputStream());
            String theString = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);


            System.out.println(" :: stream value model " + theString);

            return theString.substring(0, theString.indexOf("\r"));
        } catch (Exception e) {
            FrameworkLogger.logError(e.getMessage());
            return null;
        }
    }

    public static void writeLogInFile(String fileName, List<LogEntry> data) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                if (!file.delete()) {
                    FrameworkLogger.logFail(fileName + " file not deleted");
                }
            }
            if (!file.createNewFile()) {
                FrameworkLogger.logFail(fileName + " file not created");
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            for (LogEntry line : data) {
                fw.write(String.valueOf(line));
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to execute command
     *
     * @param command
     * @param data
     * @return
     * @throws IOException
     */
    public static List<String> runProcess_IOUtils(String data, String... command) throws IOException {
        List<String> newList = new ArrayList<>();
        String[] allCommand;
        if (System.getProperty("os.name").contains("Windows")) {
            allCommand = concat(WIN_RUNTIME, command);
        } else {
            allCommand = concat(new String[]{"bash", "-c"}, command);
        }
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(allCommand);

        //List<String> list = IOUtils.readLines(process.getInputStream());
        List<String> list = IOUtils.readLines(process.getInputStream(),StandardCharsets.UTF_8);
        for (String eachItem : list) {
            if (eachItem.contains(data)) {
                newList.add(eachItem);
            }
        }
        return newList;
    }


    public static String getMonthFromInt(int number) {
        String monthString;
        String[] str = {"Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"};
        if (number < str.length) {
            return monthString = str[number - 1];
        } else {
            return monthString = "Invalid month";
        }
    }


    public static void deleteFolder(String folderPath){

        File dir = new File(folderPath);

        if(!dir.isDirectory()) {
            dir.mkdir();
            System.out.println("Directory is created!");
            return;
        }else{
            File[] listFiles = dir.listFiles();
            assert listFiles != null;
            for(File file : listFiles){
                System.out.println("Deleting "+file.getName());
                file.delete();
            }
        }

        //now directory is empty, so we can delete it
        //System.out.println("Deleting Directory. Success = "+dir.delete());
    }


    public static void deleteFile(String filePath){

        File file = new File(filePath);

        if(file.delete())
        {
            System.out.println("File deleted successfully");
        }
        else
        {
            System.out.println("Failed to delete the file");
        }
    }

    public static void createFolder(String folderPath){
        String rootFolder = System.getProperty("user.dir");
        File file = new File(rootFolder+folderPath);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }
}