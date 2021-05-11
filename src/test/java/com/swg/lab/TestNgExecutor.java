package com.swg.lab;

import com.swg.framework.api.adb.AdbHelper;
import com.swg.framework.api.adb.CommandsFactory;
import com.swg.framework.setup.StartAppiumServer;
import com.swg.framework.utility.Util;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.swg.framework.execution.ExecutionRecordsGenerator.*;
import static com.swg.framework.setup.InitializerScript.*;

public class TestNgExecutor {
    public static List<String> portsOpen = new ArrayList<>();
    public static HashMap<String, List<String>> deviceDetails = new HashMap<>();
    public static boolean isDebugMode = Boolean.parseBoolean(Util.readFile("Project.Properties", "isDebugMode").trim());
    public static String executionMode = Util.readFile("Project.Properties", "executionMode").trim();

    public static void main(String[] args) throws Exception {
        getDevicePortInfo();

        //Create an instance on TestNG
        TestNG testSuite = new TestNG();

        //Create an instance of XML Suite and assign a name for it.
        XmlSuite xmlSuite = new XmlSuite();

        //Setting values for Suite
        xmlSuite.setParallel(XmlSuite.ParallelMode.TESTS);
        xmlSuite.setName("Suite");
        xmlSuite.setVerbose(1);
        xmlSuite.setThreadCount(deviceDetails.size());

        setTestSuiteName();
        getTestConfiguration();
        generateRecords();

        //Write test and classes based on the mode of execution


        //add the list of tests to your Suite.
        xmlSuite.setTests(writeTestsAndClasses(xmlSuite));

        //Add the suite to the list of suites.
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(xmlSuite);

        //Set the list of Suites to the testNG object you created earlier.
        testSuite.setXmlSuites(suites);
        testSuite.addListener(new TestSuiteListener());
        testSuite.setDefaultSuiteName("Suite");
        testSuite.setDefaultTestName("ClassTest");
        testSuite.setOutputDirectory(System.getProperty("user.dir") + "/test-output");

        //Print the XMLsuite created
        for (XmlSuite suite : suites) {
            createXmlFile(suite);
        }

        //invoke run() - this will run your class.
        testSuite.run();
    }

    public static class TestSuiteListener implements ISuiteListener {

        @Override
        public void onStart(ISuite suite) {
            System.out.println("TestNG suite default output directory = " + suite.getOutputDirectory());
        }

        @Override
        public void onFinish(ISuite suite) {
            System.out.println("TestNG invoked methods = " + suite.getAllInvokedMethods());
        }
    }


    /*Return the list of folder available in the directory*/
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
            foldersInDirectory.add(directoryAsFile.getName());
        }

        return foldersInDirectory;
    }

    /*
    createXmlFile(): This method can create a physical copy of xml file tha TESTNGCreates above
     */
    public void createXmlFile(String saveFilePath, XmlSuite suiteName) {
        File file = new File(saveFilePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            writer.write(suiteName.toXml());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(suiteName.toXml());
        try {
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void createXmlFile(XmlSuite mSuite) throws Exception {
        FileWriter writer;
        try {
            writer = new FileWriter(new File("testTemp.xml"));
            writer.write(mSuite.toXml());
            writer.flush();
            writer.close();
            System.out.println(new File("testTemp.xml").getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getDevicePortInfo() throws Exception {
        if (System.getProperty("os.name").contains("Windows")) {
            Util.executeCommand(CommandsFactory.getKillServerCommand());
            Util.executeCommand(CommandsFactory.getStartServerCommand());
        } else {
            Util.executeCommand("killall node");
            Util.executeCommand("killall Terminal");
        }
        List<String> deviceNames = AdbHelper.deviceName();
        List<String> osVersions = AdbHelper.deviceVersion(deviceNames);

        for (int i = 0; i < deviceNames.size(); i++) {
            List<String> deviceIDPort = new ArrayList<>();
            deviceIDPort.add(deviceNames.get(i));
            deviceIDPort.add(osVersions.get(i));
            portsOpen.add(StartAppiumServer.port[i]);
            deviceDetails.put(StartAppiumServer.port[i], deviceIDPort);
        }
         }

    public static List<XmlTest> writeTestsAndClasses(XmlSuite xmlSuite) throws Exception {

        if (isDebugMode == false) {

            //Create a list of XmlTests and add the Xmltest you created earlier to it.
            List<XmlTest> myTests = new ArrayList<XmlTest>();

            if (executionMode.toLowerCase().startsWith("p")) {

                //Create an instance of XmlTest and assign a name for it.
                for (Map.Entry<String, List<String>> entry : deviceDetails.entrySet()) {
                    XmlTest myTest = new XmlTest(xmlSuite);
                    myTest.setName("Test" + entry.getKey());
                    myTest.setPreserveOrder(true);
                    myTest.setThreadCount(1);

                    myTest.addParameter(portValueParam, entry.getKey());
                    myTest.addParameter(platformVersionNameParam, entry.getValue().get(1));
                    myTest.addParameter(deviceIDParam, entry.getValue().get(0));

                    //Create a list which can contain the classes that you want to run.
                    List<XmlClass> testClasses = new ArrayList<XmlClass>();
                    List<String> getScriptsNames = updatedSuiteCreator();
                    for (String scriptName : getScriptsNames) {
//                System.out.println("Updated Script Name :::: " + scriptName);
                        testClasses.add(new XmlClass(scriptName));
                    }


                    //Assign that to the XmlTest Object created earlier.
                    myTest.setXmlClasses(testClasses);


                    myTests.add(myTest);
                }

            }
            if (executionMode.toLowerCase().startsWith("d")) {

                //Create lists of tests
                HashMap<String, List<String>> testBlockList = new HashMap<>();
                List<String> listOfScriptNames = updatedSuiteCreator();
                int blockSize = 0;
                int remainder = 0;

                /*
                Script allocation happens here such that all ports/devices get equal no of scripts,
                but if any remainder scripts are there its get allocated to last available port/device
                 */

                blockSize = Math.round(listOfScriptNames.size() / deviceDetails.size());
                remainder = listOfScriptNames.size() % deviceDetails.size();

                for (int i = 0; i < deviceDetails.size(); i++) {
                    List<String> testCaseListPerBlock;

                    if (i == deviceDetails.size() - 1) {
                        testCaseListPerBlock = listOfScriptNames.subList(i * blockSize, (i * blockSize + blockSize + remainder));
                    } else {
                        testCaseListPerBlock = listOfScriptNames.subList(i * blockSize, (i * blockSize + blockSize));
                    }

                    testBlockList.put(portsOpen.get(i), testCaseListPerBlock);
                }

                /*if (listOfScriptNames.size() % deviceDetails.size() == 0) {
                    blockSize = Math.round(listOfScriptNames.size() / deviceDetails.size());
                } else {
                    blockSize = Math.round(listOfScriptNames.size() / deviceDetails.size()) + 1;
                }
                int lower = 0;
                for (int i = 0; i < deviceDetails.size(); i++) {
                    int upper = lower + blockSize;
                    List<String> testCaseListPerBlock = new ArrayList<>();
                    for (int j = lower; j < upper; j++) {
                        if (j < listOfScriptNames.size())
                            testCaseListPerBlock.add(listOfScriptNames.get(j));
                    }
                    testBlockList.put(portsOpen.get(i), testCaseListPerBlock);
                    lower = upper;

                }*/

                //System.out.println(testBlockList.toString());

                //Create an instance of XmlTest and assign a name for it.
                for (Map.Entry<String, List<String>> entry : deviceDetails.entrySet()) {
                    XmlTest myTest = new XmlTest(xmlSuite);
                    myTest.setName("Test" + entry.getKey());
                    myTest.setPreserveOrder(true);
                    myTest.setThreadCount(1);

                    myTest.addParameter(portValueParam, entry.getKey());
                    myTest.addParameter(platformVersionNameParam, entry.getValue().get(1));
                    myTest.addParameter(deviceIDParam, entry.getValue().get(0));

                    //Create a list which can contain the classes that you want to run.
                    List<XmlClass> testClasses = new ArrayList<XmlClass>();
                    List<String> getScriptsNames = testBlockList.get(entry.getKey());
                    for (String scriptName : getScriptsNames) {
//                System.out.println("Updated Script Name :::: " + scriptName);
                        testClasses.add(new XmlClass(scriptName));
                    }


                    //Assign that to the XmlTest Object created earlier.
                    myTest.setXmlClasses(testClasses);


                    myTests.add(myTest);
                }

            }

            if (executionMode.startsWith("s")) {

                //Create an instance of XmlTest and assign a name for it.
                XmlTest myTest = new XmlTest(xmlSuite);
                myTest.setName("Test" + StartAppiumServer.port[0]);
                myTest.setPreserveOrder(true);
                myTest.setThreadCount(1);

                myTest.addParameter(portValueParam, StartAppiumServer.port[0]);
                myTest.addParameter(platformVersionNameParam, deviceDetails.get(StartAppiumServer.port[0]).get(1));
                myTest.addParameter(deviceIDParam, deviceDetails.get(StartAppiumServer.port[0]).get(0));

                //Create a list which can contain the classes that you want to run.
                List<XmlClass> testClasses = new ArrayList<XmlClass>();
                List<String> getScriptsNames = updatedSuiteCreator();
                for (String scriptName : getScriptsNames) {
//                System.out.println("Updated Script Name :::: " + scriptName);
                    testClasses.add(new XmlClass(scriptName));
                }


                //Assign that to the XmlTest Object created earlier.
                myTest.setXmlClasses(testClasses);


                myTests.add(myTest);

            }
            return myTests;
        } else {
            System.out.println("\n\n*********'isDebugMode' is 'true' hence, execute individual scripts\n Or re-run the script with 'isDebugMode' as 'false'**********");
            System.exit(0);
            return null;
        }
    }
}