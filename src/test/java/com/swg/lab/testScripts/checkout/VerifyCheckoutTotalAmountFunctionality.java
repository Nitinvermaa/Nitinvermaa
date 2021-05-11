package com.swg.lab.testScripts.checkout;

import com.swg.framework.enums.Direction;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify checkout total amount",
        testCaseId = "SWAG_09",
        requirementID = "SWAG_09",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_09: Verify sum of Item total and tax is total
*/
public class VerifyCheckoutTotalAmountFunctionality extends BaseTestScript {
    public VerifyCheckoutTotalAmountFunctionality(){
    }
    float itemTotal=0;
    float tax=0;
    float actualTotal=0;
    float expectedTotal = 0;
    @Override
    public final void invokeTest() throws VerificationFailException {
        try {
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
        checkoutScreen.getCheckoutInfoZipTextField().sendText("00000");
        checkoutScreen.getCheckoutContinueBTN().click();
        while (!checkoutScreen.getCheckoutOverviewItemTotal().isFound(2)){
            checkoutScreen.swipeScreen(Direction.SLIDE_UP);
            if (checkoutScreen.getCheckoutOverviewItemTotal().isFound(1)){
                break;
            }
        }

            checkoutScreen.getCheckoutOverviewItemTotal().isFound(3);
            itemTotal = invoiceCalculation(checkoutScreen.getCheckoutOverviewItemTotal().getText());
            System.out.println(itemTotal);
            tax = invoiceCalculation(checkoutScreen.getCheckoutOverviewTax().getText());
            actualTotal = invoiceCalculation(checkoutScreen.getCheckoutOverviewTotal().getText());

        expectedTotal = itemTotal + tax;
        FrameworkLogger.logStep("Expected Total is +"+expectedTotal);
        FrameworkLogger.logStep("ActualTotal Total is +"+actualTotal);

        /*Verify Checkout functionality*/
        Verify.verifyTrue(actualTotal==expectedTotal,"SWAG_09", "Verify sum of Item total and tax is total");

        } catch (Exception e) {
            resultMap.put(testCaseID, "Fail");
            FrameworkLogger.logStep(e.getMessage());

        }
    }

    private static float invoiceCalculation(String amount){
        String price_prefix;
        String price_suffix ;
        String[] price = amount.split("\\$");
        price_prefix = price[0];
        price_suffix = price[1];
        FrameworkLogger.logStep("price_prefix:: "+price_prefix);
        FrameworkLogger.logStep("price_suffix:: "+price_suffix);
        return Float.parseFloat(price_suffix);
    }
}
