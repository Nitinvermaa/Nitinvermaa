package com.swg.framework.api.control;

import com.swg.framework.api.UiControl;
import com.swg.framework.enums.ElementType;
import com.swg.framework.logging.FrameworkLogger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * List class to get the list of the ids.
 */
public class List extends UiControl {

    public List(String id, ElementType type) {
        super(id, type);
    }

    public void selectDropDownByVisibleText(String text) {
        FrameworkLogger.logStep("Selecting text " + text + " to " + element);
        WebElement dropDownList = driver.findElement(element);
        Select dropdown = new Select(dropDownList);
        dropdown.selectByVisibleText(text);
    }

    public void selectDropDownByIndex(int index) {
        FrameworkLogger.logStep("Selecting index " + index + " to " + element);
        WebElement dropDownList = driver.findElement(element);
        Select dropdown = new Select(dropDownList);
        dropdown.selectByIndex(index);
    }

    public void selectDropDownByValue(String text) {
        FrameworkLogger.logStep("Selecting index " + text + " to " + element);
        WebElement dropDownList = driver.findElement(element);
        Select dropdown = new Select(dropDownList);
        dropdown.selectByValue(text);
    }


}
