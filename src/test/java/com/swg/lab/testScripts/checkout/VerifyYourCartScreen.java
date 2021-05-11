package com.swg.lab.testScripts.checkout;

import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify Your cart screen ",
        testCaseId = "SWAG_04",
        requirementID = "SWAG_04",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_04: Verify Your cart screen
*/
public class VerifyYourCartScreen extends BaseTestScript {
    public VerifyYourCartScreen(){
    }
    @Override
    public final void invokeTest() throws VerificationFailException {

        FrameworkLogger.logStep("Logging into the application");
        loginToApp();
        String prodName = productsScreen.getAddProdByNameToCartBtn("Sauce Labs Backpack").getText();
        productsScreen.getAddProdByNameToCartBtn("Sauce Labs Backpack").click();
        loginScreen.getLoginCARTItemCount().click();

        /*Verify Your cart screen */
        Verify.verifyTrue(commonScreen.getLabelControl(prodName).isFound(2),
                "SWAG_04","Verify Your cart screen ");
    }
}
