package com.swg.framework.api.control;

import com.swg.framework.api.UiControl;
import com.swg.framework.enums.ElementType;
import com.swg.framework.logging.FrameworkLogger;

public class Link extends UiControl {
    String elementIdentifier;
    ElementType selectorType;

    public Link(String id, ElementType type) {
        super(id, type);
        elementIdentifier = id;
        selectorType = type;

    }

    public void click() {
        driver.findElement(element).click();
        FrameworkLogger.logStep("Clicking on " + element);
    }
}
