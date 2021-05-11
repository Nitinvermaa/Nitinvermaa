package com.swg.lab.screens;


import com.swg.framework.api.ScreenPattern;
import com.swg.framework.api.control.Button;
import com.swg.framework.api.control.Label;

/**
 * UI extraction for Cart Screen.
 */
public abstract class CartScreen extends ScreenPattern {

    /**
     * Constructor
     */
    public CartScreen() {
        super();
    }


    /*
     * Holds Your Cart label
     */
    public abstract Button getYourCartLabel();

    /*
     * Holds First product on Your Cart Screen
     */
    public abstract Label getYourCartFirstProdName();

    /*
     * Holds First product on Your Cart Screen
     */
    public abstract Button getYourCartCheckoutBtn();
}
