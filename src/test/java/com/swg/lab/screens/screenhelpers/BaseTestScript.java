package com.swg.lab.screens.screenhelpers;

import com.swg.framework.api.AndroidDriverHelper;
import com.swg.framework.api.adb.CommandsFactory;
import com.swg.framework.enums.Direction;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.setup.InitializerScript;
import com.swg.framework.utility.Util;
import com.swg.lab.screens.CartScreen;
import com.swg.lab.screens.CheckoutScreen;
import com.swg.lab.screens.LoginScreen;
import com.swg.lab.screens.ProductsScreen;
import io.appium.java_client.android.Activity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringReader;

/**
 * Base test script for common activities for every script.
 */
public class BaseTestScript extends InitializerScript {

    private static String className;
    protected LoginScreen loginScreen;
    protected AndroidDriverHelper androidDriverHelper;
    protected CommonScreen commonScreen;
    protected ScreenFactory screenFactory;
    protected CartScreen cartScreen;
    protected CheckoutScreen checkoutScreen;
    protected ProductsScreen productsScreen;

    public BaseTestScript() {
        screenFactory = new ScreenFactory();
        androidDriverHelper = new AndroidDriverHelper();

    }

    private void initScreens() {
        loginScreen = screenFactory.getScreen(ScreenFactory.LOGIN_SCREEN);
        commonScreen = screenFactory.getScreen(ScreenFactory.COMMON_SCREEN);
        cartScreen = screenFactory.getScreen(ScreenFactory.CART_SCREEN);
        checkoutScreen = screenFactory.getScreen(ScreenFactory.CHECKOUT_SCREEN);
        productsScreen = screenFactory.getScreen(ScreenFactory.PRODUCTS_SCREEN);

    }

    /***
     * Configuration method for HH signup and precondition
     *
     * @throws Exception: Throws exception
     */
    @Override
    public void configuration() throws Exception {
        initScreens();
    }


    /**
     * Method to start Android activity.
     */
    public void startAndroidActivity() {
        FrameworkLogger.logStep("Start Activity");
        getAndroidDriver().startActivity(new Activity(packageName, activityName));
    }

    /**
     * Method to start android activity in current state
     */
    public void startAndroidActivityInCurrentState() {
        FrameworkLogger.logStep("Start HH Activity on " + device.get() + " in it's current state");
        String com = "adb -s " + device.get() + " shell am start -n " + packageName + "/" + activityName;
        Util.executeCommand(com);
    }

    protected void stopAndroidActivity() {
        FrameworkLogger.logStep("Close Activity on " + device.get() + " in it's current state");
        String com = "adb shell am force-stop " + packageName;
        Util.executeCommand(com);
    }

    protected boolean sendADBEnterKey() throws InterruptedException {
        Util.runProcess(CommandsFactory.getHitKeyboardEnter(device.get()));
        return true;

    }

    /*
    Gets text content either by accessing content-desc or text
     */
    protected static String getTextValue(Element e) {
        String textContent = e.getAttribute("content-desc");
        if (textContent.length() <= 0) {
            textContent = e.getAttribute("text");
        }
        return textContent;
    }

    /*
    convertStringToXMLDocument() : Parses String to XML
     */
    protected static Document convertStringToXMLDocument(String xmlString) throws Exception {
//        Parser that produces DOM object trees from XML content
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        //API to obtain DOM Document instance
        javax.xml.parsers.DocumentBuilder builder = null;
        //Create DocumentBuilder with default configuration
        builder = factory.newDocumentBuilder();
        org.xml.sax.InputSource is = new org.xml.sax.InputSource();
        is.setCharacterStream(new StringReader(xmlString));
        //Parse the content to Document object
        org.w3c.dom.Document doc = builder.parse(is);

        return doc;
    }

    public void loginToApp() throws VerificationFailException {
        FrameworkLogger.logStep("Waiting for app to launch");
        loginScreen.getLoginDefinedUser().isFound(2);
        while (!loginScreen.getLoginDefinedUser().isFound(2)){
            loginScreen.swipeScreen(Direction.SLIDE_UP);
            if (loginScreen.getLoginDefinedUser().isFound(1)){

                break;
            }
        }
        loginScreen.getLoginDefinedUser().click();
        while (!loginScreen.getLoginButton().isFound(2)){
            loginScreen.swipeScreen(Direction.SLIDE_DOWN);
            if (loginScreen.getLoginButton().isFound(1)){

                break;
            }
        }

        loginScreen.getLoginButton().click();
        FrameworkLogger.logStep("Clicking on Login Button");
    }
}


