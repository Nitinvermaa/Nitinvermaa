package com.swg.framework.enums;

/**
 * IOS send keys.
 */
public enum IOSSendKeys {

    BACKSPACE("8"),
    ENTER("13"),
    RETURN("13"),
    SPACE("32");

    public String value;

    IOSSendKeys(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }

}
