package com.swg.lab.testScripts.checkout;

import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify empty cart",
        testCaseId = "SWAG_06",
        requirementID = "SWAG_06",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_06: Verify empty cart
*/
public class VerifyEmptyCartFunctionality extends BaseTestScript {
    public VerifyEmptyCartFunctionality(){
    }
    @Override
    public final void invokeTest() throws VerificationFailException {

        FrameworkLogger.logStep("Logging into the application");
        loginToApp();
        checkoutScreen.getEmptyCartIcon().click();
        /*Verify empty cart*/

        Verify.verifyFalse(cartScreen.getYourCartLabel().isFound(2),
                "SWAG_06","Verify empty cart");
    }
}
