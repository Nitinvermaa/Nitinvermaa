package com.swg.framework.execution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Class for Header Data information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface HeaderData {


    String EMPTY_VALUE = "empty";
    boolean DEFAULT_VALUE = false;

    /**
     * Specifies possible data types for data driven tests<br/>
     * <p>
     * DataType.EXCEL - test data are contained in an Excel file<br/>
     * DataType.JSON - test data are contained in a Json file<br/>
     * DataType.XML - test data are contained in an Xml file<br/>
     * DataType.EMPTY - test data are absent<br/>
     * </p>
     */
    enum DataType {
        EXCEL,
        JSON,
        XML,
        CSV,
        EMPTY
    }

    enum Configuration {
        SWGLAB_IOS, SWGLAB_ANDROID
    }

    enum ExecutableFor {
        NO,
        IOS,
        ANDROID,
        WEB
    }

    enum Suite {
        Regression,
        Sanity,
        P1,
        P2,
        NonFunctional
    }

    /**
     * Configuration Enum for giving information about the configuration to run in.
     */
    Configuration[] configuration();

    /**
     * Giving information about Test script.
     */
    String testScriptDescription();

    /**
     * Test case id of that script.
     */
    String testCaseId();

    /**
     * requirement id of that script.
     */
    String requirementID();

    /**
     * A type of test data
     */
    DataType dataType() default DataType.EMPTY;

    /**
     * A path to test data
     */
    String testDataPath() default EMPTY_VALUE;

    /**
     * Command time out
     */
    String commandTimeout() default EMPTY_VALUE;

    /**
     * Command Script Status
     */
    ExecutableFor[] executableFor();

    /**
     * Suite Name
     */
    Suite[] suite();

    /**
     * Reset required
     */
    boolean isResetRequired() default DEFAULT_VALUE;
}