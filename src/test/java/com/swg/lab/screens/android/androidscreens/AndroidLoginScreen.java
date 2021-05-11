package com.swg.lab.screens.android.androidscreens;


import com.swg.framework.api.control.Button;
import com.swg.framework.api.control.Label;
import com.swg.framework.api.control.TextField;
import com.swg.framework.enums.ElementType;
import com.swg.lab.screens.LoginScreen;

import static com.swg.lab.screens.android.locators.Locators.LoginLocators.*;

/**
 * UI extraction for Landing Screen.
 */
public class AndroidLoginScreen extends LoginScreen {

    @Override
    public TextField getLoginUserName() {
        return getCachedControl(TextField.class, LOGIN_USERNAME, ElementType.byXpath);
    }

    @Override
    public TextField getLoginPassword() {
        return getCachedControl(TextField.class, LOGIN_PASSWORD, ElementType.byXpath);
    }

    @Override
    public Button getLoginButton() {
        return getCachedControl(Button.class, LOGIN_BUTTON, ElementType.byXpath);
    }

    @Override
    public Button getLoginDefinedUser() {
        return getCachedControl(Button.class, LOGIN_DEFINED_USER, ElementType.byXpath);
    }



    @Override
    public Label getLoginCARTItemCount() {
        return getCachedControl(Label.class, LOGIN_CART_INDEX, ElementType.byXpath);
    }



}
