package com.swg.lab.screens.android.androidscreens;


import com.swg.framework.api.control.Button;
import com.swg.framework.api.control.Label;
import com.swg.framework.api.control.TextField;
import com.swg.framework.enums.ElementType;
import com.swg.lab.screens.CheckoutScreen;
import com.swg.lab.screens.ProductsScreen;

import static com.swg.lab.screens.android.locators.Locators.CHECKOUTLocators.*;

/**
 * UI extraction for Products Screen.
 */
public class AndroidCheckoutScreen extends CheckoutScreen {


    /*
     * Holds First Name Checkout screen
     */
    @Override
    public TextField getCheckoutInfoFirstNameTextField() {
        return getCachedControl(TextField.class, CHECKOUT_INFO_FIRST_NAME_TEXTFIELD, ElementType.byXpath);
    }


    /*
     * Holds Last Name text field Checkout screen
     */
    @Override
    public TextField getCheckoutInfoLastNameTextField() {
        return getCachedControl(TextField.class, CHECKOUT_INFO_LAST_NAME_TEXTFIELD, ElementType.byXpath);
    }

    /*
     * Holds ZIP code Checkout screen
     */
    @Override
    public TextField getCheckoutInfoZipTextField() {
        return getCachedControl(TextField.class, CHECKOUT_INFO_ZIP_TEXTFIELD, ElementType.byXpath);
    }

    /*
     * Holds Continue Button Checkout screen
     */
    @Override
    public Button getCheckoutContinueBTN() {
        return getCachedControl(Button.class, CHECKOUT_INFO_CONTINUE_BTN, ElementType.byXpath);
    }

    /*
     * Holds Continue Your Cart Checkout BTN
     */
    @Override
    public Button getCheckoutBTN() {
        return getCachedControl(Button.class, CHECKOUT_YOUR_CART_CHECKOUT_BTN, ElementType.byXpath);
    }

    /*Checkout CartIcon BTN*/
    @Override
    public Button getCARTIcon() {
        return getCachedControl(Button.class, LOGIN_CART_ICON, ElementType.byXpath);
    }

    /*Checkout Empty CartIcon BTN*/
    @Override
    public Button getEmptyCartIcon() {
        return getCachedControl(Button.class, LOGIN_Empty_CART_ICON, ElementType.byXpath);
    }
    /*
     * Holds Continue Overview Button Checkout screen
     */
    @Override
    public Button getCheckoutOverviewContinueBTN() {
        return getCachedControl(Button.class, CHECKOUT_OVERVIEW_CONTINUE_BTN, ElementType.byXpath);
    }

    /*
     * Holds Continue Overview Item Total Checkout screen
     */
    @Override
    public Label getCheckoutOverviewItemTotalLabel() {
        return getCachedControl(Label.class, CHECKOUT_OVERVIEW_ITEM_TOTAL_LABEL, ElementType.byXpath);
    }

    /*
     * Holds Continue Overview Tax Label Checkout screen
     */
    @Override
    public Label getCheckoutOverviewTaxLabel() {
        return getCachedControl(Label.class, CHECKOUT_OVERVIEW_TAX_LABEL, ElementType.byXpath);
    }

    /*
     * Holds Continue Overview Total Label Checkout screen
     */
    @Override
    public Label getCheckoutOverviewTotalLabel() {
        return getCachedControl(Label.class, CHECKOUT_OVERVIEW_TOTAL_LABEL, ElementType.byXpath);
    }

    /*
     * Holds Continue Info Finish BTN Checkout screen
     */
    @Override
    public Label getCheckoutInfoFinishBTN() {
        return getCachedControl(Label.class, CHECKOUT_INFO_FINISH_BTN, ElementType.byXpath);
    }

    /*
     * Holds Continue Complete Label Checkout screen
     */
    @Override
    public Label getCheckoutCompleteLabel() {
        return getCachedControl(Label.class, CHECKOUT_COMPLETE_LABEL, ElementType.byXpath);
    }

    /*
     * Holds Continue Complete Back Home Checkout screen
     */
    @Override
    public Button getCheckoutCompleteBackHomeBTN() {
        return getCachedControl(Button.class, CHECKOUT_COMPLETE_BACK_HOME_BTN, ElementType.byXpath);
    }

    /*
     * Holds Item Total price
     */
    @Override
    public Label getCheckoutOverviewItemTotal() {
        return getCachedControl(Label.class, CHECKOUT_OVERVIEW_ITEM_TOTAL, ElementType.byXpath);
    }

    /*
     * Holds Tax on Item Total price
     */
    @Override
    public Label getCheckoutOverviewTax() {
        return getCachedControl(Label.class, CHECKOUT_OVERVIEW_TAX, ElementType.byXpath);
    }

    /*
     * Holds Total price
     */
    @Override
    public Label getCheckoutOverviewTotal() {
        return getCachedControl(Label.class, CHECKOUT_OVERVIEW_TOTAL, ElementType.byXpath);
    }
}
