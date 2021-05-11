package com.swg.lab.testScripts.checkout;

import com.swg.framework.enums.Direction;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify Checkout functionality",
        testCaseId = "SWAG_05",
        requirementID = "SWAG_05",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_05: Verify Checkout functionality
*/
public class VerifyCheckoutFunctionality extends BaseTestScript {
    public VerifyCheckoutFunctionality(){
    }
    @Override
    public final void invokeTest() throws VerificationFailException {

        FrameworkLogger.logStep("Logging into the application");
        loginToApp();
        productsScreen.getAddProdByNameToCartBtn("Sauce Labs Backpack").click();
        productsScreen.getAddProdByNameToCartBtn("Sauce Labs Bike Light").click();
        checkoutScreen.getCARTIcon().click();
        while (!checkoutScreen.getCheckoutBTN().isFound(2)){
            checkoutScreen.swipeScreen(Direction.SLIDE_UP);
            if (checkoutScreen.getCheckoutBTN().isFound(1)){
                break;
            }
        }
        checkoutScreen.getCheckoutBTN().click();
        checkoutScreen.getCheckoutInfoFirstNameTextField().sendText("FirstName");
        checkoutScreen.getCheckoutInfoLastNameTextField().sendText("LastName");
        checkoutScreen.getCheckoutInfoZipTextField().sendText("00000");
        checkoutScreen.getCheckoutContinueBTN().click();
        while (!checkoutScreen.getCheckoutInfoFinishBTN().isFound(2)){
            checkoutScreen.swipeScreen(Direction.SLIDE_UP);
            if (checkoutScreen.getCheckoutInfoFinishBTN().isFound(1)){
                break;
            }
        }

        checkoutScreen.getCheckoutInfoFinishBTN().click();
        /*Verify Checkout functionality*/
        Verify.verifyTrue(checkoutScreen.getCheckoutCompleteLabel().isFound(3),"SWAG_05", "Verify Checkout functionality");
    }
}
