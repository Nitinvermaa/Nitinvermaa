package com.swg.lab.testScripts.login;

import com.swg.framework.enums.Direction;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.logging.Verify;
import com.swg.lab.screens.screenhelpers.BaseTestScript;

@HeaderData(
        configuration = {HeaderData.Configuration.SWGLAB_ANDROID},
        testScriptDescription = "Verify user should be able to login into the application",
        testCaseId = "SWAG_01",
        requirementID = "SWAG_01",
        commandTimeout = "720",
        isResetRequired = false,
        suite = HeaderData.Suite.Regression,
        executableFor = {HeaderData.ExecutableFor.ANDROID}
)

/*
(1)SWAG_01: Verify user should be able to login into the application
*/
public class VerifyLoginFunctionality extends BaseTestScript {
    public VerifyLoginFunctionality(){
    }
    @Override
    public final void invokeTest() throws VerificationFailException {

        loginScreen.getLoginDefinedUser().isFound(2);
        loginScreen.swipeScreen(Direction.SLIDE_UP);
        loginScreen.getLoginDefinedUser().click();
        loginScreen.swipeScreen(Direction.SLIDE_DOWN);
        loginScreen.getLoginButton().click();
        /*Verify login functionality - Products Label is displayed*/
        Verify.verifyTrue(productsScreen.getFirstProdNameLabel().isFound(30),
                "SWAG_01","Verify login functionality");
    }
}
