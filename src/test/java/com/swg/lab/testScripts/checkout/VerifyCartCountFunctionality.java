package com.swg.lab.testScripts.checkout;

import com.swg.framework.enums.Direction;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify cart count when  user click ADD TO CART button",
        testCaseId = "SWAG_02",
        requirementID = "SWAG_02",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_02: Verify cart count when user click ADD TO CART button
*/
public class VerifyCartCountFunctionality extends BaseTestScript {
    public VerifyCartCountFunctionality(){
    }
    @Override
    public final void invokeTest() throws VerificationFailException {

        FrameworkLogger.logStep("Logging into the application");
        loginToApp();
        productsScreen.getFirstProdAddToCartBtn().click();
        /*Verify cart count when user click ADD TO CART button*/
        int cartCount = Integer.parseInt(loginScreen.getLoginCARTItemCount().getText());
        Verify.verifyTrue(cartCount>0,
                "SWAG_02","Verify cart count when user click ADD TO CART button");
    }
}
