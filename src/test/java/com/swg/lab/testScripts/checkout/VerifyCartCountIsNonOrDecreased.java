package com.swg.lab.testScripts.checkout;

import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify cart count is non or decreased by 1",
        testCaseId = "SWAG_03",
        requirementID = "SWAG_03",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_03: Verify cart count when user click ADD TO CART button
*/
public class VerifyCartCountIsNonOrDecreased extends BaseTestScript {
    public VerifyCartCountIsNonOrDecreased(){
    }
    @Override
    public final void invokeTest() throws VerificationFailException {

        FrameworkLogger.logStep("Logging into the application");
        loginToApp();
        productsScreen.getFirstProdAddToCartBtn().click();
        productsScreen.getFirstProdRemoveBTN().click();
        /*Verify cart count is non or decreased by 1*/
        Verify.verifyFalse(loginScreen.getLoginCARTItemCount().isFound(3),
                "SWAG_03", "Verify cart count is non or decreased by 1");
    }
}
