package com.swg.lab.screens.android.androidscreens;

import com.swg.framework.api.control.Label;
import com.swg.framework.enums.ElementType;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.lab.screens.screenhelpers.CommonScreen;

/**
 * UI extraction for Landing Screen.
 */
public class AndroidCommonScreen extends CommonScreen {


    /**
     * Holds generic label control for exact match
     */
    @Override
    public Label getLabelControl(String text) {
        return getCachedControl(Label.class, "//*[@text='" + text + "']", ElementType.byXpath);

    }

    /**
     * Holds generic label control for partial match
     */
    @Override
    public Label getPartialMatchLabelControl(String text) {
        return getCachedControl(Label.class, "//*[contains(@text,'" + text + "')]", ElementType.byXpath);
    }

    /**
     * Method to select text
     */
    @Override
    public void selectText(String text) {
        FrameworkLogger.logStep("Select " + text);
        this.getLabelControl(text).click();
    }



}

