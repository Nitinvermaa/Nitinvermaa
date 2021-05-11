package com.swg.lab.screens;


import com.swg.framework.api.ScreenPattern;
import com.swg.framework.api.control.Button;
import com.swg.framework.api.control.Label;
import com.swg.framework.api.control.TextField;

/**
 * UI extraction for Landing Screen.
 */
public abstract class CheckoutScreen extends ScreenPattern {

    /**
     * Constructor
     */
    public CheckoutScreen() {
        super();
    }


    /*
     * Holds First Name Checkout screen
     */
    public abstract TextField getCheckoutInfoFirstNameTextField();

    /*
     * Holds Last Name text field Checkout screen
     */
    public abstract TextField getCheckoutInfoLastNameTextField();

    /*
     * Holds ZIP code Checkout screen
     */
    public abstract TextField getCheckoutInfoZipTextField();

    /*
     * Holds Continue Button Checkout screen
     */
    public abstract Button getCheckoutContinueBTN();

    /*
     * Holds Continue Your Cart Checkout BTN
     */
    public abstract Button getCheckoutBTN();



    /*Checkout CartIcon BTN*/
    public abstract Button getCARTIcon();

    /*Checkout Empty CartIcon BTN*/
    public abstract Button getEmptyCartIcon();

    /*
     * Holds Continue Overview Button Checkout screen
     */
    public abstract Button getCheckoutOverviewContinueBTN();

    /*
     * Holds Continue Overview Item Total Checkout screen
     */
    public abstract Label getCheckoutOverviewItemTotalLabel();

    /*
     * Holds Continue Overview Tax Label Checkout screen
     */
    public abstract Label getCheckoutOverviewTaxLabel();

    /*
     * Holds Continue Overview Total Label Checkout screen
     */
    public abstract Label getCheckoutOverviewTotalLabel();

    /*
     * Holds Continue Info Finish BTN Checkout screen
     */
    public abstract Label getCheckoutInfoFinishBTN();

    /*
     * Holds Continue Complete Label Checkout screen
     */
    public abstract Label getCheckoutCompleteLabel();

    /*
     * Holds Continue Complete Back Home Checkout screen
     */
    public abstract Button getCheckoutCompleteBackHomeBTN();


    /*
     * Holds Item Total price
     */
    public abstract Label getCheckoutOverviewItemTotal();

    /*
     * Holds Tax on Item Total price
     */
    public abstract Label getCheckoutOverviewTax();

    /*
     * Holds Total price
     */
    public abstract Label getCheckoutOverviewTotal();
}
