package com.swg.framework.logging;

//import junit.framework.Assert;

import com.swg.framework.setup.InitializerScript;
import org.testng.Assert;

/**
 * This class holds a set of methods to verify different kinds of objects
 */
public class Verify {

    private static Integer screenCaptureIndex = 1;

    /**
     * Verifies if the object is true, raise an exception otherwise
     */
    public static void verifyTrue(Boolean object) {
        verifyEquals(object, true);
    }

    /**
     * Verifies if the object is true, raise an exception otherwise
     * Add description message in the log
     */
    public static void verifyTrue(Boolean object, String message) {
        verifyEquals(object, true, InitializerScript.testCaseID, message);
    }

    /**
     * Verifies if the object is true, raise an exception otherwise
     * Add description message in the log
     */
    public static void verifyTrue(Boolean object, String testID, String message) {
        verifyEquals(object, true, testID, message);
    }

    /**
     * Verifies if the object is false, raise an exception otherwise
     */
    public static void verifyFalse(Boolean object) {
        verifyEquals(object, false);
    }

    /**
     * Verifies if the object is false, raise an exception otherwise
     * Add description message in the log
     */
    public static void verifyFalse(Boolean object, String message) {
        verifyEquals(object, false, InitializerScript.testCaseID, message);
    }

    /**
     * Verifies if the object is false, raise an exception otherwise
     * Add description message in the log
     */
    public static void verifyFalse(Boolean object, String testID, String message) {
        verifyEquals(object, false, testID, message);
    }

    /**
     * Verifies if 2 objects are equal
     * Raise an exception in case when the objects are not equal, if it is not suppressed by suppressException parameter
     * Add description message in the log
     */
    public static void verifyEquals(Object actual, Object expected, String testID, String message, Boolean suppressException) {
        InitializerScript.testCaseID = testID;
        try {
            Assert.assertEquals(expected, actual);
            FrameworkLogger.logPass(String.format("%s - %s EXPECTED: '%s' ACTUAL: '%s'",
                    testID, message, expected.toString(), actual.toString()));

            if (InitializerScript.resultMap.containsKey(testID)) {
                if (InitializerScript.resultMap.get(testID).equalsIgnoreCase("Fail")) {
                    InitializerScript.resultMap.put(testID, "Fail");
                } else {
                    InitializerScript.resultMap.put(testID, "Pass");
                }
            } else {
                InitializerScript.resultMap.put(testID, "Pass");
            }
        } catch (Throwable error) {
            InitializerScript.resultMap.put(testID, "Fail");
            fail(testID + " - " + message, error.getMessage(), suppressException);
        }
    }

    /**
     * Verifies if 2 objects are equal,
     * Always raise an exception in case when the objects are not equal
     */
    public static void verifyEquals(Object actual, Object expected) {
        verifyEquals(actual, expected, InitializerScript.testCaseID, "", false);
    }

    /**
     * Verifies if 2 objects are equal
     * Raise an exception in case when the objects are not equal, if it is not suppressed by suppressException parameter
     */
    public static void verifyEquals(Object actual, Object expected, Boolean suppressException) {
        verifyEquals(actual, expected, InitializerScript.testCaseID, "", suppressException);
    }

    /**
     * Verifies if 2 objects are equal,
     * Always raise an exception in case when the objects are not equal
     * Add description message in the log
     */
    public static void verifyEquals(Object actual, Object expected, String message) {
        verifyEquals(actual, expected, InitializerScript.testCaseID, message, false);
    }

    /**
     * Verifies if 2 objects are equal,
     * Always raise an exception in case when the objects are not equal
     * Add description message in the log
     */
    public static void verifyEquals(Object actual, Object expected, String testID, String message) {
        verifyEquals(actual, expected, testID, message, false);

    }

    /**
     * Verifies if 2 objects are NOT equal
     * Raise an exception in case when the objects are equal, if it is not suppressed by suppressException parameter
     * Add description message in the log
     */
    private static void verifyNotEquals(Object actual, Object expected, String testID, String message, Boolean suppressException) {
        InitializerScript.testCaseID = testID;
        try {
            Assert.assertEquals(expected, actual);
            String errorMessage = String.format("expected: %s but was: %s", expected.toString(), actual.toString());
            InitializerScript.resultMap.put(testID, "Fail");
            fail(testID + " - " + message, errorMessage, suppressException);
        } catch (AssertionError error) {
            FrameworkLogger.logPass(String.format("%s - %s ACTUAL: '%s' and EXPECTED: '%s'.",
                    testID, message, actual.toString(), expected.toString()));
            if (InitializerScript.resultMap.containsKey(testID)) {
                if (InitializerScript.resultMap.get(testID).equalsIgnoreCase("Fail")) {
                    InitializerScript.resultMap.put(testID, "Fail");
                } else {
                    InitializerScript.resultMap.put(testID, "Pass");
                }
            } else {
                InitializerScript.resultMap.put(testID, "Pass");
            }
        }
    }

    /**
     * Verifies if 2 objects are NOT equal,
     * Always raise an exception in case when the objects are equal
     */
    public static void verifyNotEquals(Object actual, Object expected) {
        verifyNotEquals(actual, expected, InitializerScript.testCaseID, "", false);
    }

    /**
     * Verifies if 2 objects are NOT equal
     * Raise an exception in case when the objects are equal, if it is not suppressed by suppressException parameter
     */
    public static void verifyNotEquals(Object actual, Object expected, Boolean suppressException) {
        verifyNotEquals(actual, expected, InitializerScript.testCaseID, "", suppressException);
    }

    /**
     * Verifies if 2 objects are NOT equal,
     * Always raise an exception in case when the objects are equal
     * Add description message in the log
     */
    public static void verifyNotEquals(Object actual, Object expected, String message) {
        verifyNotEquals(actual, expected, InitializerScript.testCaseID, message, false);
    }

    /**
     * Verifies if 2 objects are NOT equal,
     * Always raise an exception in case when the objects are equal
     * Add description message in the log
     */
    public static void verifyNotEquals(Object actual, Object expected, String testID, String message) {
        verifyNotEquals(actual, expected, testID, message, false);
    }

    /**
     * The method is used when verification if fail
     * Capture an device screen image with unique sequence number in the name
     * Create and write a fail message into the log
     * Raise an exception if suppressException parameter is true
     */
    private static void fail(String descriptionMessage, String errorMessage, Boolean suppressException) {

        // Build the error message.
        StringBuilder combinedMessage = new StringBuilder(descriptionMessage);
        if (descriptionMessage != null && !descriptionMessage.isEmpty()) {
            combinedMessage.append("\n");
        }
        combinedMessage.append(" VERIFICATION FAILS BETWEEN OBJECTS: ");
        combinedMessage.append(errorMessage);

        //Write message to the log file with error message
        FrameworkLogger.logFail(combinedMessage.toString());

        //Throw new Verification fail exception if it is not suppressed
        if (!suppressException) {
            //InitializerScript.errorCollector.addError((new VerificationFailException("message")));
            try {
                InitializerScript.captureScreenshot();
            } catch (Exception e) {
                e.printStackTrace();
            }
            InitializerScript.isScreenShotCaptured = true;
            InitializerScript.status.set(false);
            //throw new VerificationFailException(combinedMessage.toString());
        }
    }


}
