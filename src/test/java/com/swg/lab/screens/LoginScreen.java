package com.swg.lab.screens;


import com.swg.framework.api.ScreenPattern;
import com.swg.framework.api.control.Button;
import com.swg.framework.api.control.Label;
import com.swg.framework.api.control.TextField;

/**
 * UI extraction for Login Screen.
 */
public abstract class LoginScreen extends ScreenPattern {

    /**
     * Constructor
     */
    public LoginScreen() {
        super();
    }

    /*
    * Holds LOGIN_USERNAME
    */
    public abstract TextField getLoginUserName();

    /*
     * Holds LOGIN_PASSWORD
     */
    public abstract TextField getLoginPassword();

    /*
     * Holds LOGIN_BUTTON
     */
    public abstract Button getLoginButton();

    /*
     * Holds LOGIN_DEFINED_USER
     */
    public abstract Button getLoginDefinedUser();


    /*
     * Holds LOGIN_CART_ICON
     */
    public abstract Label getLoginCARTItemCount();


}
