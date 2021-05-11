package com.swg.lab;

import com.swg.framework.utility.Util;

import static com.swg.framework.execution.HeaderToExcel.createNewExcelFile;
import static com.swg.framework.execution.HeaderToExcel.updateSuiteXLS;


public class SuiteCreator {
    public static void main(String[] args) throws Exception {
        String rootFolder = System.getProperty("user.dir");

        /*Deleting Report folder*/
        Util.deleteFolder(rootFolder + "/Reports");
        /*Deleting Logs folder*/
        Util.deleteFolder(rootFolder + "/Logs");

        Util.createFolder("/Logs/Snapshots/");

        Util.clearProjectProperty(System.getProperty("user.dir") + "/Report.Properties");
        Util.setProjectProperty("Report.Properties", "executionReportPath", "");

        //Loading each classes and finding the header information from it.
        //Creating mobile test suite excel if not present
        createNewExcelFile();
        //Updating mobile test suite excel
        updateSuiteXLS();
    }
}
