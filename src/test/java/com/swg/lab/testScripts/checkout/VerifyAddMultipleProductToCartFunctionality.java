package com.swg.lab.testScripts.checkout;

import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify Add Multiple Product To cart",
        testCaseId = "SWAG_08",
        requirementID = "SWAG_08",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_08: Verify Add Multiple Product To cart
*/
public class VerifyAddMultipleProductToCartFunctionality extends BaseTestScript {
    public VerifyAddMultipleProductToCartFunctionality(){
    }
    @Override
    public final void invokeTest() throws VerificationFailException {

        FrameworkLogger.logStep("Logging into the application");
        loginToApp();
        productsScreen.getAddProdByNameToCartBtn("Sauce Labs Backpack").click();
        productsScreen.getAddProdByNameToCartBtn("Sauce Labs Bike Light").click();
        loginScreen.getLoginCARTItemCount().isFound(2);
        /*Verify cart count when user click ADD TO CART button*/
        int cartCount = Integer.parseInt(loginScreen.getLoginCARTItemCount().getText());
        Verify.verifyTrue(cartCount>0, "SWAG_08","Verify Add Multiple Product To cart");
    }
}
