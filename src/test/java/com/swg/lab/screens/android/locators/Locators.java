package com.swg.lab.screens.android.locators;


/**
 * Contains elements locator
 */
public class Locators {

    public static final class LoginLocators{
        public static final String LOGIN_USERNAME = "//android.widget.EditText[@text='Username']";
        public static final String LOGIN_PASSWORD = "//android.widget.EditText[@text='Password']";
        public static final String LOGIN_BUTTON = "//android.view.ViewGroup[@content-desc='test-LOGIN']";
        public static final String LOGIN_DEFINED_USER = "//android.widget.TextView[@text='standard_user']";

        public static final String LOGIN_CART_INDEX = "//*[@content-desc='test-Cart']/./android.view.ViewGroup/android.widget.TextView";
    }

    public static final class ProductLocators{
        public static final String PRODUCTS_LABEL = "//*[@text='PRODUCTS']";
        public static final String FIRST_PRODUCT_ADD_TO_CART_BTN = "//*[@text='Sauce Labs Backpack']/../android.view.ViewGroup[@content-desc='test-ADD TO CART']";
        public static final String FIRST_PRODUCT_NAME= "//*[@text='Sauce Labs Backpack']";
        public static final String FIRST_PRODUCT_REMOVE_BTN= "//*[@text='REMOVE']";

    }

    public static final class CARTLocators{
        public static final String CART_YOUR_CART_LABEL = "//*[@text='YOUR CART']";
        public static final String YOUR_CART_FIRST_PRODUCT_NAME= "//*[@text='Sauce Labs Backpack']";
        public static final String YOUR_CART_CHECKOUT_BTN= "//*[@text='CHECKOUT']";

    }

    public static final class CHECKOUTLocators{
        public static final String CHECKOUT_INFO_FIRST_NAME_TEXTFIELD= "//*[@content-desc='test-First Name']";
        public static final String CHECKOUT_INFO_LAST_NAME_TEXTFIELD= "//*[@content-desc='test-Last Name']";
        public static final String CHECKOUT_INFO_ZIP_TEXTFIELD= "//*[@content-desc='test-Zip/Postal Code']";

        public static final String LOGIN_CART_ICON = "//*[@content-desc='test-Cart']/./android.view.ViewGroup/android.widget.TextView";
        public static final String LOGIN_Empty_CART_ICON = "//*[@content-desc='test-Cart']";

        public static final String CHECKOUT_INFO_CONTINUE_BTN= "//*[@text='CONTINUE']";
        public static final String CHECKOUT_YOUR_CART_CHECKOUT_BTN= "//*[@text='CHECKOUT']";

        public static final String CHECKOUT_OVERVIEW_CONTINUE_BTN= "//*[@text='CHECKOUT: OVERVIEW']";
        public static final String CHECKOUT_OVERVIEW_ITEM_TOTAL_LABEL= "//*[@text='Payment Information:']/../android.widget.TextView[5]";
        public static final String CHECKOUT_OVERVIEW_TAX_LABEL= "//*[@text='Payment Information:']/../android.widget.TextView[6]";
        public static final String CHECKOUT_OVERVIEW_TOTAL_LABEL= "//*[@text='Payment Information:']/../android.widget.TextView[7]";
        public static final String CHECKOUT_INFO_FINISH_BTN= "//*[@text='FINISH']";
        public static final String CHECKOUT_COMPLETE_LABEL= "//*[@text='CHECKOUT: COMPLETE!']";
        public static final String CHECKOUT_COMPLETE_BACK_HOME_BTN= "//*[@text='BACK HOME']";
        public static final String CHECKOUT_OVERVIEW_ITEM_TOTAL= "//*[@content-desc='test-CANCEL']/../../android.widget.TextView[5]";
        public static final String CHECKOUT_OVERVIEW_TAX= "//*[@content-desc='test-CANCEL']/../../android.widget.TextView[6]";
        public static final String CHECKOUT_OVERVIEW_TOTAL= "//*[@content-desc='test-CANCEL']/../../android.widget.TextView[7]";




    }


}