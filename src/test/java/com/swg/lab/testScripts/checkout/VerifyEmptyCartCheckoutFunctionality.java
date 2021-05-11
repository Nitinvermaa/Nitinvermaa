package com.swg.lab.testScripts.checkout;

import com.swg.framework.enums.Direction;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify Checkout functionality when cart is empty ",
        testCaseId = "SWAG_07",
        requirementID = "SWAG_07",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_07: Verify Checkout functionality when cart is empty
*/
public class VerifyEmptyCartCheckoutFunctionality extends BaseTestScript {
    public VerifyEmptyCartCheckoutFunctionality(){
    }
    @Override
    public final void invokeTest() throws VerificationFailException {

        FrameworkLogger.logStep("Logging into the application");
        loginToApp();
        checkoutScreen.getEmptyCartIcon().click();
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
        Verify.verifyFalse(checkoutScreen.getCheckoutCompleteLabel().isFound(3),
                "SWAG_07", "Verify User should not be with checkout process when cart count is zero or non");
    }
}
