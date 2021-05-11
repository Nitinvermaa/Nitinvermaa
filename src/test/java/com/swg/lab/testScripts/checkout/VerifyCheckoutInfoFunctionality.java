package com.swg.lab.testScripts.checkout;

import com.swg.framework.enums.Direction;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.framework.utility.RandomUtils;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify CheckoutInfo Functionality",
        testCaseId = "SWAG_10",
        requirementID = "SWAG_10",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_10: Verify Checkout Information zip code should not accept alphabet
*/
public class VerifyCheckoutInfoFunctionality extends BaseTestScript {
    public VerifyCheckoutInfoFunctionality(){
    }
    @Override
    public final void invokeTest() throws Exception {

        FrameworkLogger.logStep("Logging into the application");
        loginToApp();
        productsScreen.getFirstProdAddToCartBtn().click();
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
        FrameworkLogger.logStep("Entering Alphabets in Zip field");
        checkoutScreen.getCheckoutInfoZipTextField().sendText(RandomUtils.generateRandomString(10, RandomUtils.Mode.ALPHA));
        checkoutScreen.getCheckoutContinueBTN().click();
        while (!checkoutScreen.getCheckoutInfoFinishBTN().isFound(2)){
            checkoutScreen.swipeScreen(Direction.SLIDE_UP);
            if (checkoutScreen.getCheckoutInfoFinishBTN().isFound(1)){
                break;
            }
        }

        checkoutScreen.getCheckoutInfoFinishBTN().click();
        /*Verify Checkout Information zip code should not accept alphabet*/
        Verify.verifyFalse(checkoutScreen.getCheckoutCompleteLabel().isFound(3),"SWAG_10", "Verify Checkout Information zip code should not accept alphabet");
    }
}
