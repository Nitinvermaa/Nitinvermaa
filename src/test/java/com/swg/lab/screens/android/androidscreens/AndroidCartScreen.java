package com.swg.lab.screens.android.androidscreens;


import com.swg.framework.api.control.Button;
import com.swg.framework.api.control.Label;
import com.swg.framework.enums.ElementType;
import com.swg.lab.screens.CartScreen;
import com.swg.lab.screens.ProductsScreen;
import static com.swg.lab.screens.android.locators.Locators.CARTLocators.*;


/**
 * UI extraction for Cart Screen.
 */
public class AndroidCartScreen extends CartScreen {


    /*
     * Holds Your Cart label
     */
    @Override
    public Button getYourCartLabel() {
        return getCachedControl(Button.class, CART_YOUR_CART_LABEL, ElementType.byXpath);
    }


    /*
     * Holds First product on Your Cart Screen
     */
    @Override
    public Label getYourCartFirstProdName() {
        return getCachedControl(Label.class, YOUR_CART_FIRST_PRODUCT_NAME, ElementType.byXpath);
    }

    /*
     * Holds First product on Your Cart Screen
     */
    @Override
    public Button getYourCartCheckoutBtn() {
        return getCachedControl(Button.class, YOUR_CART_CHECKOUT_BTN, ElementType.byXpath);
    }



}
