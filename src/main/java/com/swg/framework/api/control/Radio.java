package com.swg.framework.api.control;

import com.swg.framework.api.UiControl;
import com.swg.framework.enums.ElementType;
import com.swg.framework.logging.FrameworkLogger;

/**
 * Checkbox class giving is checked method.
 */
public class Radio extends UiControl {

    public Radio(String id, ElementType type) {
        super(id, type);
    }

    public boolean isChecked() {
        FrameworkLogger.logStep("Check if " + element + "is selected");
        return driver.findElement(element).getAttribute("checked").equalsIgnoreCase("true");
    }

}
