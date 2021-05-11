package com.swg.lab.screens.screenhelpers;

import com.swg.framework.api.ScreenPattern;
import com.swg.framework.api.control.Label;

/**
 * UI extraction for Landing Screen.
 */
public abstract class CommonScreen extends ScreenPattern {

    /**
     * Constructor
     */
    public CommonScreen() {
        super();
    }

    /**
     * Holds generic label control
     */
    public abstract Label getLabelControl(String text);


    public abstract Label getPartialMatchLabelControl(String text);

    public abstract void selectText(String text);


}
