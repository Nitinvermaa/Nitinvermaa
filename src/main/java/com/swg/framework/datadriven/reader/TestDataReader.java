package com.swg.framework.datadriven.reader;


import com.swg.framework.datadriven.model.DataContainer;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.setup.InitializerScript;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Specify a way to read test data
 */
public abstract class TestDataReader {

    public static final String FILE_PREFIX = "file:/";
    public static final String SLASH_SIGN = "/";

    /**
     * Reads test data based on the class of concrete test
     *
     * @param clazz - a class object
     * @param <T>   - a type of the test class
     * @return {@link
     * @see
     */
    public <T extends InitializerScript> DataContainer readTestData(Class<T> clazz) throws ParseException {
        String name = clazz.getSimpleName();
        URL testDataUrl = clazz.getResource(SLASH_SIGN + name + getResolution());
        return readTestData(testDataUrl);
    }

    public <T extends InitializerScript> DataContainer readTestData(String path) throws ParseException {
        URL fileUrl = null;
        try {
            fileUrl = new URL(FILE_PREFIX + path);
        } catch (MalformedURLException e) {
            FrameworkLogger.logError(e.toString());
        }
        return readTestData(fileUrl);
    }

    /**
     * Returns a resolution of the file with test data
     *
     * @return {@link String}
     */
    protected abstract String getResolution();

    /**
     * Reads test data from the given URL
     *
     * @param testDataUrl an url of test data
     * @return {@link
     * @see
     */
    protected abstract <T extends InitializerScript> DataContainer readTestData(URL testDataUrl) throws ParseException;

    /**
     * Adds information about the type of test data and its path to the log
     *
     * @param dataType a type of test data
     * @param path     a path to test data
     */
    public static void logTestDataInfo(String dataType, String path) {
        FrameworkLogger.logStep(String.format("Data Driven test has been started with following test data - {type = '%s'; path = '%s'}", dataType, path));
    }
}
