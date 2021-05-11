package com.swg.lab.screens.screenhelpers;

import com.swg.framework.common.Constants;
import com.swg.framework.logging.FrameworkLogger;

import com.swg.framework.utility.Util;
import com.swg.lab.screens.*;
import com.swg.lab.screens.android.androidscreens.*;

/**
 * Screen factory class
 */
public class ScreenFactory {

    public static final String LOGIN_SCREEN = "login_screen";
    public static final String COMMON_SCREEN = "common_screen";
    public static final String CART_SCREEN = "cart_screen";
    public static final String CHECKOUT_SCREEN = "checkout_screen";
    public static final String PRODUCTS_SCREEN = "products_screen";
    private LoginScreen loginScreen = null;
    private CommonScreen commonScreen = null;
    private CartScreen cartScreen = null;
    private ProductsScreen productsScreen = null;
    private CheckoutScreen checkoutScreen = null;



    /**
     * Method return Implementation class as per screen name
     */
    public <T> T getScreen(final String screenName) {
        try {
            String platformName = Util.readProperties("Platform");
            if (platformName.equalsIgnoreCase(Constants.ANDROID)) {
               if (screenName.equalsIgnoreCase(COMMON_SCREEN)) {
                    commonScreen = new AndroidCommonScreen();
                    return (T) commonScreen;
                } else if (screenName.equalsIgnoreCase(LOGIN_SCREEN)) {
                   loginScreen = new AndroidLoginScreen();
                   return (T) loginScreen;
               }else if (screenName.equalsIgnoreCase(CART_SCREEN)) {
                   cartScreen = new AndroidCartScreen();
                   return (T) cartScreen;
               }else if (screenName.equalsIgnoreCase(CHECKOUT_SCREEN)) {
                   checkoutScreen = new AndroidCheckoutScreen();
                   return (T) checkoutScreen;
               }else if (screenName.equalsIgnoreCase(PRODUCTS_SCREEN)) {
                   productsScreen = new AndroidProductsScreen();
                   return (T) productsScreen;
               }
            } else {
                FrameworkLogger.logFail("Invalid Platform");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}