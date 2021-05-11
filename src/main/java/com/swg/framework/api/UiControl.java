package com.swg.framework.api;

import com.swg.framework.api.adb.AdbHelper;
import com.swg.framework.api.commonUI.Container;
import com.swg.framework.common.Constants;
import com.swg.framework.enums.ElementType;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.setup.InitializerScript;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Ui control class.
 */
public abstract class UiControl extends Container {
    public WebDriver driver;
    public AndroidDriver androidDriver;
    public AppiumDriver appiumDriver;
    public IOSDriver iosDriver;
    String elementIdentifier;
    ElementType selectorType;
    public By element;

    protected UiControl(String id, ElementType type) {
        driver = InitializerScript.getWebDriver();
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            this.driver = InitializerScript.getAndroidDriver();
        } else if (InitializerScript.platform.get().equalsIgnoreCase(Constants.IOS)) {
            this.driver = InitializerScript.getIOSDriverDriver();
        } else if (InitializerScript.platform.get().equalsIgnoreCase(Constants.WEB)) {
            this.driver = (WebDriver) driver;
        }
        this.selectorType = type;
        this.elementIdentifier = id;

        switch (selectorType) {
            case byId:
                element = By.id(elementIdentifier);
                break;
            case byName:
                element = By.name(elementIdentifier);
                break;
            case byXpath:
                element = By.xpath(elementIdentifier);
                break;
            case byClass:
                element = By.className(elementIdentifier);
                break;
            case byCssSelector:
                element = By.cssSelector(elementIdentifier);
                break;
            default:
                break;
        }
    }

    public void selectInList(int index) {
        //TODO: Implement this

        /*List<WebElement> list =(appiumDriver) driver.findElementsById("");
        list.get(index).click();*/
    }

    public void click() {
        FrameworkLogger.logStep("Clicking on " + element);
        driver.findElement(element).click();
    }

    public void longClick() {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
           // androidDriver.tap(1, findElements().get(0), 750);
            new TouchAction(androidDriver).tap((TapOptions) driver.findElement(element)).perform();

        } else {
           // iosDriver.tap(1, iosDriver.findElement(element), 5000);
            new TouchAction(iosDriver).tap((TapOptions) driver.findElement(element)).perform();

        }

        // or use below
       /* TouchActions action = new TouchActions(androidDriver);
        action.longPress(driver.findElement(element));
        action.perform(); */


    }

    public String getText() {
        FrameworkLogger.logStep("Getting text from " + element);
        return driver.findElement(element).getText();
    }

    public boolean isFound(Integer timeInSec) {
        FrameworkLogger.logStep("Checking if " + element + " is found");
        try {
            Long timeOut = Long.valueOf(timeInSec);
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
            if (driver.findElement(element).isDisplayed()) {
                driver.manage().timeouts().implicitlyWait(InitializerScript.implicitTimeOut, TimeUnit.SECONDS);
                return true;
            } else {
                FrameworkLogger.logStep(element + " NOT FOUND !!");
                return false;
            }
        } catch (Exception e) {
            FrameworkLogger.logStep(element + " NOT FOUND !!");
            driver.manage().timeouts().implicitlyWait(InitializerScript.implicitTimeOut, TimeUnit.SECONDS);
            return false;
        }
    }

    public String getAttribute(String key) {
        FrameworkLogger.logStep("Getting property " + key + " from element " + element);
        return driver.findElement(element).getAttribute(key);
    }

    public boolean isEnabled() {
        FrameworkLogger.logStep("Checking if element " + element + " is enabled");
        return driver.findElement(element).isEnabled();
    }


    public void await() {
        try {
            FrameworkLogger.logStep("Waiting for " + element);
//          TODO: Check class not found error for below webdriver wait logic

            driver.wait();
        } catch (Exception e) {
            FrameworkLogger.logError("Failed while getting text from " + element);
        }
    }

    public Response execute(String driverCommand, Map<String, ?> parameters) {

        //return driver.execute(driverCommand, parameters);
        return null;
    }

    public ExecuteMethod getExecuteMethod() {

        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {

            return androidDriver.getExecuteMethod();
        } else {
            return iosDriver.getExecuteMethod();

        }

    }

    public void resetApp() {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            androidDriver.resetApp();
        } else {
            androidDriver.resetApp();
        }

    }

    public boolean isAppInstalled(String bundleId) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            return androidDriver.isAppInstalled(bundleId);
        } else {
            return iosDriver.isAppInstalled(bundleId);
        }
    }

    public void installApp(String appPath) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            androidDriver.installApp(appPath);
        } else {
            iosDriver.installApp(appPath);
        }
    }

    public void removeApp(String bundleId) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            androidDriver.removeApp(bundleId);
        } else {
            iosDriver.removeApp(bundleId);
        }
    }

    public void launchApp() {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            androidDriver.launchApp();
            FrameworkLogger.logStep("Launch Application");
        } else {
            iosDriver.launchApp();
            FrameworkLogger.logStep("Launch Application");
        }
    }

    public void closeApp() {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            FrameworkLogger.logStep("Close Application");
            androidDriver.closeApp();
        } else {
            FrameworkLogger.logStep("Close Application");
            iosDriver.closeApp();
        }
    }

    public void runAppInBackground(int seconds) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
           // androidDriver.runAppInBackground(seconds);
        } else {
            //iosDriver.runAppInBackground(seconds);
        }
    }





    public TouchAction performTouchAction(TouchAction touchAction) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {

            return androidDriver.performTouchAction(touchAction);
        } else {
            return iosDriver.performTouchAction(touchAction);
        }

    }

    public void performMultiTouchAction(MultiTouchAction multiAction) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            androidDriver.performMultiTouchAction(multiAction);
        } else {
            iosDriver.performMultiTouchAction(multiAction);
        }
    }


    public void tap(WebElement element, int duration) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            //androidDriver.tap(1, element, duration);
            new TouchAction(androidDriver).tap((TapOptions) element).perform();

        } else {
            //iosDriver.tap(1, element, duration);
        }
    }

    public void tap(int x, int y, int duration) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            //androidDriver.tap(1, x, y, duration);
            new TouchAction(androidDriver)
                    .tap(PointOption.point(x, y))
                    .perform();

        } else {
            //iosDriver.tap(1, x, y, duration);
        }
    }




    public WebDriver context(String name) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            return androidDriver.context(name);
        } else {
            return iosDriver.context(name);
        }

    }

    public Set<String> getContextHandles() {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            return androidDriver.getContextHandles();
        } else {
            return iosDriver.getContextHandles();
        }
    }

    public String getContext() {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            return androidDriver.getContext();
        } else {
            return iosDriver.getContext();
        }
    }


    public WebElement findElementByAccessibilityId(String using) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            return androidDriver.findElementByAccessibilityId(using);
        } else {
            return iosDriver.findElementByAccessibilityId(using);
        }
    }

    public List<WebElement> findElementsByAccessibilityId(String using) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            return androidDriver.findElementsByAccessibilityId(using);
        } else {
            return iosDriver.findElementsByAccessibilityId(using);
        }
    }

    public Location location() {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            return androidDriver.location();
        } else {
            return iosDriver.location();
        }
    }

    public void setLocation(Location location) {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            androidDriver.setLocation(location);
        } else {
            iosDriver.setLocation(location);
        }
    }



    public URL getRemoteAddress() {
        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            return androidDriver.getRemoteAddress();
        } else {
            return iosDriver.getRemoteAddress();
        }
    }

    public List<WebElement> findElements() {
        return driver.findElements(element);
    }

    public void clearField() {
        driver.findElement(element).clear();
    }

    public boolean explicitWaitForAnElement(int seconds) {
        new WebDriverWait(driver, seconds).until(ExpectedConditions
                .presenceOfElementLocated(element));
        return true;
    }

    public boolean isDisplayed() {
        FrameworkLogger.logStep("Checking if element " + element + " is displayed");
        return driver.findElement(element).isDisplayed();
    }

    public boolean waitForElementToDisappear() {
        try {
            while (driver.findElement(element).isEnabled()) {
                Thread.sleep(3000);
            }
        } catch (NoSuchElementException e) {
            FrameworkLogger.logStep("No Such element exception was found.");
            return false;
        } catch (InterruptedException e) {
            FrameworkLogger.logStep("Interrupted exception was found.");
            return false;
        } catch (Exception ex) {
            FrameworkLogger.logStep("Exception was found.");
            return false;
        }
        return true;
    }

    public boolean waitFor(int time) {
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
        return true;
    }

    public int checkIfTileUnread() {
        try {
            WebElement urElement = driver.findElement(element);
            if (urElement != null) {

                int endIndex = urElement.getText().indexOf("New");
                String countString = urElement.getText().substring(1, endIndex).trim();
                if (countString.equalsIgnoreCase("")) {
                    return 0;
                } else {
                    return Integer.parseInt(countString);
                }
            } else {
                return 0;
            }
        } catch (NoSuchElementException NSE) {
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean doubleTapImage() {
        performTapAction();
        return true;
    }

    public void performTapAction() {
        Dimension size = InitializerScript.getDriver().manage().window().getSize();
        AdbHelper.doubleTap(size.getHeight() / 2, size.getWidth() / 2);
        // driver.execute("mobile: tap", tapObject);
    }
}
