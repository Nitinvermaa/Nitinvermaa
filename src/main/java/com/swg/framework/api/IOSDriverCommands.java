package com.swg.framework.api;

import com.swg.framework.setup.InitializerScript;
import io.appium.java_client.ios.IOSDriver;

/**
 * IOS driver commands class.
 */
public class IOSDriverCommands {

    IOSDriver iosDriver;

    /**
     * Constructor
     */
    public IOSDriverCommands() {
        this.iosDriver = InitializerScript.getIOSDriverDriver();
    }

    /**
     * Scroll to the element whose 'text' attribute contains the input text.
     *
     * @param text input text contained in text attribute
     */
    /*public void scrollTo(String text) {
        iosDriver.scrollTo(text);
    }*/

    /**
     * Scroll to the element whose 'text' attribute is equal to the input text.
     *
     * @param text input text to match
     */
   /* public void scrollToExact(String text) {
        iosDriver.scrollToExact(text);
    }*/

    /**
     * Hides the keyboard if it is showing.
     *
     * @param strategy HideKeyboardStrategy
     * @param keyName  A String, representing the text displayed on the button of the keyboard you want to press.
     *                 For example: "Done"
     */
    public void hideKeyboard(String strategy, String keyName) {
        //TODO: need to know the usages/ working of this method from appium site
        // iosDriver.hideKeyboard(strategy, keyName);
    }

    /**
     * Hides the keyboard by pressing the button specified by keyName if it is showing.
     *
     * @param keyName The button pressed by the mobile driver to attempt hiding the keyboard
     */
    public void hideKeyboard(String keyName) {
        //TODO: need to know the usages/ working of this method from appium site
        //iosDriver.hideKeyboard(keyName);
    }

    /**
     * Simulate shaking the device
     */
    public void shake() {
        iosDriver.shake();
    }

    /**
     * This is a convenience method for getting the named TextField, rather than its containing element.
     *
     * @param name accessibility id of TextField
     * @return The text field with the given accessibility id
     */
   /* public WebElement getNamedTextField(String name) {

        return iosDriver.getNamedTextField(name);
    }

    public WebElement findElementByIosUIAutomation(String using) {
        return iosDriver.findElementByIosUIAutomation(using);
    }

    public List<WebElement> findElementsByIosUIAutomation(String using) {
        //TODO: need to know the usages/ working of this method from appium site
        return iosDriver.findElementsByIosUIAutomation(using);
    }*/

}
