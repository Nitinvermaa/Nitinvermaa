package com.swg.framework.api.control;

import com.swg.framework.api.UiControl;
import com.swg.framework.enums.ElementType;
import com.swg.framework.logging.FrameworkLogger;

/**
 * Button class giving click and getId methods.
 */
public class Button extends UiControl {
    String elementIdentifier;
    ElementType selectorType;

    public Button(String id, ElementType type) {
        super(id, type);
        elementIdentifier = id;
        selectorType = type;

    }

    public void click() {
        driver.findElement(element).click();
        FrameworkLogger.logStep("Clicking on " + element);
    }

    public String getId() {
        return elementIdentifier;
    }

}


