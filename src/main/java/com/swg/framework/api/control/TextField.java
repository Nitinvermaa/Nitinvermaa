package com.swg.framework.api.control;

import com.swg.framework.api.UiControl;
import com.swg.framework.enums.ElementType;
import com.swg.framework.logging.FrameworkLogger;
import io.appium.java_client.android.AndroidElement;

/**
 * Text field class to send text from its id.
 */
public class TextField extends UiControl {

    public TextField(String id, ElementType type) {
        super(id, type);
    }

    public void sendText(String text) {
        FrameworkLogger.logStep("Sending text " + text + " to " + element);
        // driver.findElement(element).clear();
        driver.findElement(element).sendKeys(text);
    }

    public void setValue(String text) {
        FrameworkLogger.logStep("Sending text " + text + " to " + element);
        // driver.findElement(element).clear();
        AndroidElement el = (AndroidElement) driver.findElement(element);
        el.replaceValue(text);
    }

    public void clearText() {
        FrameworkLogger.logStep("Clear text " + element);
        driver.findElement(element).clear();
    }

    public void clearTextByKeyboardEvent() {
        FrameworkLogger.logStep("Clear text by keyboard " + element);
        do {
            driver.findElement(element).click();
            String text = driver.findElement(element).getText();
            int maxChars = text.length();
            for (int i = 0; i < maxChars; i++) {
                driver.findElement(element).clear();
            }
        } while (!driver.findElement(element).getText().isEmpty());
    }

}
