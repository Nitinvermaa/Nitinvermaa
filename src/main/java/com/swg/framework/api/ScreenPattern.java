package com.swg.framework.api;

import com.swg.framework.api.commonUI.Container;
import com.swg.framework.common.Constants;
import com.swg.framework.enums.Direction;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.logging.VerificationFailException;
import com.swg.framework.setup.InitializerScript;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.ElementOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.naming.TimeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

/**
 * Base class for screen object classes
 */
public class ScreenPattern extends Container {
    private AppiumDriver driver;
    private AndroidDriver androidDriver;
    private WebDriver webDriver;
    private IOSDriver iosDriver;

    public ScreenPattern() {
        this.driver = InitializerScript.getDriver();

        if (InitializerScript.platform.get().equalsIgnoreCase(Constants.ANDROID)) {
            this.androidDriver = (AndroidDriver) driver;
        } else if (InitializerScript.platform.get().equalsIgnoreCase(Constants.IOS)) {
            this.iosDriver = (IOSDriver) driver;
        } else if (InitializerScript.platform.get().equalsIgnoreCase(Constants.WEB)) {
            this.webDriver = (WebDriver) driver;
        }
    }

    public void zoom(int x, int y) {
        FrameworkLogger.logStep("Zooming current screen by " + x + "x" + y);
        //driver.zoom(x, y);
    }

    public void pinch(int x, int y) {
        FrameworkLogger.logStep("Pinch current screen by " + x + "x" + y);
        // driver.pinch(x, y);
    }

    /**
     * Method to swipe the screen.
     *
     * @param startX   : startX
     * @param startY   : startY
     * @param endX     : endX
     * @param endY:    endY
     * @param duration : duration
     */
    public void swipe(int startX, int startY, int endX, int endY, int duration) {
        FrameworkLogger.logStep("Swiping current screen by co-ordinates");
        //driver.swipe(startX, startY, endX, endY, duration);
        new TouchAction(driver)
                .press(point(startX, startY))
                .waitAction(waitOptions(ofMillis(duration)))
                .moveTo(point(endX, endY))
                .release()
                .perform();
    }

    /*
    swipeByElement(): Helps to swipe screen from a co-ordinate of element 2 and moves toward element 1.
     */
    public void swipeByElement(WebElement e2, WebElement e1, int duration) throws Exception {

        FrameworkLogger.logStep("In method swipeByElement from element2 to element1");
        TouchAction action0 = (new TouchAction(driver))
                .press(point(e2.getLocation().getX(), e2.getLocation().getY()))
                .waitAction(waitOptions(ofMillis(duration)))
                .moveTo(point(e1.getLocation().getX(), e1.getLocation().getY()))
                .release();
        action0.perform();


        /*TouchAction swipe = new TouchAction(BaseTestScript.getDriver())
                .press(point(p2.getX(), p2.getY()))
                .waitAction(waitOptions(ofSeconds(2)))
                .moveTo(point(p2.getX(), p2.getY()))
                .release().perform();*/

    }


    /*
    swipeFromElement(): Helps to swipe screen from a element position to another element
     */
    public void swipeFromElement(WebElement bottomElement, WebElement topElement, int duration) throws Exception {

        FrameworkLogger.logStep("In method swipeFromElement from element2 to element1");
        TouchAction action0 = (new TouchAction(driver))
                .press(ElementOption.element(bottomElement))
                .waitAction(waitOptions(ofMillis(duration)))
                .moveTo(ElementOption.element(topElement))
                .release();
        action0.perform();

    }


    /*
   pressAndSelect(): performs press option on element, used for multiSelectRecords and other methods
    */
    public void pressAndSelect(WebElement webElement, int duration) throws Exception {

        FrameworkLogger.logStep("In method pressAndSelect by element");
        new TouchAction(driver)
                .press(element(webElement))
                .waitAction(waitOptions(ofMillis(duration)))
                .release()
                .perform();

    }

    /*
   pressByCoordinate(): Does press action on the web element by co-ordinate that is sent as parameter
    */
    public void pressByCoordinate(int x, int y, int duration) throws Exception {

        FrameworkLogger.logStep(" In pressByCoordinate action");

        new TouchAction(driver)
                .press(point(x, y))
                //.waitAction(waitOptions(ofMillis(duration)))
                .release()
                .perform();

    }


    /*
        tapOnElement() : Method is used when click() method doesn't work to click/tap on an element
        */
    public void tapOnElement(WebElement webElement) throws Exception {
        FrameworkLogger.logStep("tap on element");
        Point p = webElement.getLocation();
        //        driver.get().tap(1, p.getX(), p.getY(), 500);
        new TouchAction(driver)
                .tap(point(p.getX(), p.getY()))
                .waitAction(waitOptions(ofMillis(500)))
                .perform();
    }

    /*
       tapByCoordinate() : Taps on element based on its X and Y co-ordinate
       */
    public void tapByCoordinate(int x, int y, int duration) throws Exception {
        FrameworkLogger.logStep(" In Tap By co-ordinate, X=" + x + " ,Y=" + y);
        //TODO: check if .release option is required here
        new TouchAction(driver).
                tap(point(x, y))
                .waitAction(waitOptions(ofMillis(duration)))
                .perform();

    }

    /*
    longPressOnWebelement(): Does long press on the web element that is sent as parameter
     */
    public void longPressOnWebelement(WebElement webElement, int duration) throws Exception {
        //        driver.get().tap(1, webElement, 1000);
        FrameworkLogger.logStep(" In longPressOnWebelement action");

        new TouchAction(driver)
                .longPress(element(webElement))
                .waitAction(waitOptions(ofMillis(duration)))
                .release()
                .perform();
        Thread.sleep(1000);

    }

    /*
    longPressByCoordinate(): Does long press on the web element by co-ordinate that is sent as parameter
     */
    public void longPressByCoordinate(int x, int y, int duration) throws Exception {

        FrameworkLogger.logStep(" In longPressByCoordinate action");

        new TouchAction(driver)
                .longPress(point(x, y))
                .waitAction(waitOptions(ofMillis(duration)))
                .release()
                .perform();

    }

    /**
     * Method to drag and drop element on the screen.
     *
     * @param startX   : startX
     * @param startY   : startY
     * @param endX     : endX
     * @param endY:    endY
     * @param duration : duration
     */
    public void dragAndDrop(int startX, int startY, int endX, int endY, int duration) {
        FrameworkLogger.logStep("Drag and drop element");
        //driver.swipe(startX, startY, endX, endY, duration);
        new TouchAction(driver)
                .longPress(point(startX, startY))
                .waitAction(waitOptions(ofMillis(duration)))
                .moveTo(point(endX, endY))
                .release()
                .perform();


    }


    /**
     * Method to Click device back button
     */
    public void back() {
        FrameworkLogger.logStep("clicking device back");
        driver.navigate().back();
    }

    public void scrollToExact(String strLocator) {
        // driver.scrollToExact(strLocator);

//        androidDriver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + strLocator + "\").instance(0))");
//
//        try {
//
//            MobileElement el = (MobileElement) androidDriver
//                    .findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + strLocator + "\").instance(0))");
//            el.click();
//
//        } catch (Exception e) {
//            System.out.println("Cannot scroll further");
//        }

        //String selectorString = String.format("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text("+ strLocator +"))");
        // driver.findElement(MobileBy.AndroidUIAutomator(selectorString));
        try {

            MobileElement el = (MobileElement) InitializerScript.getAndroidDriver()
                    .findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"" + strLocator + "\"));");
        } catch (Exception e) {
            System.out.println("Cannot scroll further");
        }


        //androidDriver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text(\"" + strLocator + "\").instance(0))");

    }

    /**
     * Method scrolls for exact menu option
     *
     * @param control Uicontrol object
     * @param menu    menu name
     * @throws InterruptedException
     */
    /*public void swipeWhileNotFoundForMenu(UiControl control, String menu) throws InterruptedException {
        List<WebElement> list = control.findElements();
        if (list.size() > 2) {
            WebElement el = list.get(0), e2 = list.get(2);

            if (!isContactVisible(control, menu)) {
                Integer maxSteps = 2;
                do {
                    TouchAction action0 = (new TouchAction(driver)).press(e2.getLocation().getX(), e2.getLocation().getY())
                            .waitAction(500).moveTo(el.getLocation().getX(), el.getLocation().getY()).release();
                    action0.perform();
                    Thread.sleep(1000l);
                    maxSteps--;
                } while (maxSteps > 0 && !isContactVisible(control, menu));

                maxSteps = 2;
                while (!isContactVisible(control, menu) && maxSteps > 0) {
                    TouchAction action0 = (new TouchAction(driver)).press(el).moveTo(0, 40).release();
                    action0.perform();
                    Thread.sleep(1000l);
                    maxSteps--;
                }
            }
        } else {
            FrameworkLogger.logStep("Home screen not found");
        }
    }
*/

    /**
     * Method checks is contact visible
     */
    public boolean isContactVisible(UiControl control, String contactName) {
        boolean isContactVisible = false;
        List<WebElement> list1 = control.findElements();
        for (WebElement obj : list1) {
            if (obj.getText() != null && obj.getText().equals(contactName)) {
                isContactVisible = true;
                break;
            }
        }
        return isContactVisible;
    }


    public void sendKeys(int key) {
        AndroidKey androidKey = null;
        switch (key) {
            case 4:
                androidKey = AndroidKey.HOME;
                break;
            case 66:
                androidKey = AndroidKey.ENTER;
                break;
            case 27:
                androidKey = AndroidKey.CAMERA;
                break;
            case 111:
                androidKey = AndroidKey.ESCAPE;
                break;
        }
        androidDriver.pressKey(new KeyEvent(androidKey));
    }

    /**
     * Method to Find elements by control
     *
     * @param control : Uicontrol element
     * @return : List of WebElement
     * @throws Exception
     */
    public List<WebElement> getContactNameList(UiControl control) throws Exception {
        if (control.isFound(30)) {
            return control.findElements();
        } else {
            FrameworkLogger.logFail("No Contact Found");
            throw new Exception("No Contact Found");
        }
    }

    /**
     * Method to scroll screen to bottom
     *
     * @param control UIControl object
     * @throws Exception
     */
    public void scrollToBottom(UiControl control) throws Exception {
        int screenHeight = driver.manage().window().getSize().getHeight();
        int screenWidth = driver.manage().window().getSize().getWidth();
        long startTime = System.currentTimeMillis();
        long maxWaitTime = 120000;
        int duration = 500;
        if (InitializerScript.isAndroid()) {
            duration = 300;
        }
        while (!control.isFound(2) && (System.currentTimeMillis() - startTime) < maxWaitTime) {
            /*TouchAction action0 = (new TouchAction(driver)).press(screenWidth / 2, (int) (screenHeight * .8))
                    .waitAction(duration).moveTo(screenWidth / 2, (int) (screenHeight * .3)).release();
            action0.perform();*/
            swipe(screenWidth / 2, (int) (screenHeight * .8), screenWidth / 2, (int) (screenHeight * .3), duration);
        }

    }

    /**
     * Method to get CAB Contact List
     *
     * @param contactId  : contact element control
     * @param totalCount : Total Count element
     * @return : Contact List
     * @throws Exception
     */
    public List<String> getCABContactNameList(UiControl contactId, UiControl totalCount) throws Exception {
        List<WebElement> list = getContactNameList(contactId);
        List<String> contactList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        final long maxWaitTime = 120000;
        if (InitializerScript.isAndroid()) {
            boolean isTotalCountFound;
            while (true) {
                for (WebElement element : list) {
                    if (!contactList.contains(element.getText())) {
                        contactList.add(element.getText());
                    }
                }
                isTotalCountFound = totalCount.isFound(5);
                if (isTotalCountFound || ((System.currentTimeMillis() - startTime) > maxWaitTime)) {
                    break;
                }
                WebElement el = list.get(1), e2 = list.get(2);
                /*TouchAction action0 = (new TouchAction(driver)).press(e2.getLocation().getX(), e2.getLocation().getY())
                        .waitAction(300).moveTo(el.getLocation().getX(), el.getLocation().getY()).release();
                */
                swipeByElement(e2, el, 0);

                Thread.sleep(1000l);
                list = contactId.findElements();
            }
        } else {
            WebElement el = null;

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isDisplayed()) {
                    el = list.get(i);
                    if (!contactList.contains(list.get(i).getText())) {
                        contactList.add(list.get(i).getText());
                    }
                } else {
                    int height = el.getSize().getHeight();
                    if (el != null) {
                        /*TouchAction action0 = (new TouchAction(driver)).press(el.getLocation().getX(), el.getLocation().getY())
                           .waitAction(500).moveTo(el.getLocation().getX(), el.getLocation().getY() - (height * 3)).release();

                        new TouchAction(driver).press(PointOption.point(el.getLocation().getX(), el.getLocation().getY())).moveTo(PointOption.point(el.getLocation().getX(), el.getLocation().getY() - (height * 3))).release().perform();
                        */
                        swipe(el.getLocation().getX(), el.getLocation().getY(), el.getLocation().getX(), el.getLocation().getY() - (height * 3), 500);

                        Thread.sleep(1000l);
                    }

                    if (!contactList.contains(list.get(i).getText())) {
                        contactList.add(list.get(i).getText());
                    }
                    list = contactId.findElements();
                }
            }
        }
        return contactList;
    }

    public void scrollToTop(UiControl control) throws Exception {
        int screenHeight = driver.manage().window().getSize().getHeight();
        int screenWidth = driver.manage().window().getSize().getWidth();

        long startTime = System.currentTimeMillis();
        final long maxWaitTime = 120000;
        int duration = 500;
        if (InitializerScript.isAndroid()) {
            duration = 300;
        }
        while (!control.isFound(2) && (System.currentTimeMillis() - startTime) < maxWaitTime) {
            /*TouchAction action0 = (new TouchAction(driver)).press(screenWidth / 2, (int) (screenHeight * .3))
                    .waitAction(duration).moveTo(screenWidth / 2, (int) (screenHeight * .9)).release();
            action0.perform();*/
            swipe(screenWidth / 2, (int) (screenHeight * .3), screenWidth / 2, (int) (screenHeight * .9), duration);

        }


    }

    /**
     * Method to wait till element not found.
     */
    public void waitForElement(Integer time, UiControl control) throws TimeLimitExceededException, InterruptedException {
        boolean isElementFound = false;
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < time) {
            if (control.isFound(5)) {
                isElementFound = true;
                break;
            }
        }
        if (!isElementFound) {
            throw new TimeLimitExceededException("Element Not Found in Given Time");
        }
    }

    //Added for record screen swipe
    public void swipeScreen(Direction direction) throws VerificationFailException {
        InitializerScript.getDriver().context("NATIVE_APP");
        Dimension size = InitializerScript.getDriver().manage().window().getSize();
        int startx = 0;
        int endx = 0;
        int starty = 0;
        int endy = 0;
        switch (direction) {
            case SLIDE_LEFT:
                startx = (int) (size.width * 0.8);
                endx = (int) (size.width * 0.025);
                starty = size.height / 2;
                //BaseTestScript.getDriver().swipe(startx, starty, endx, starty, 300);

                swipe(startx, starty, endx, starty, 300);
                break;
            case SLIDE_RIGHT:
                startx = (int) (size.width * 0.025);
                endx = (int) (size.width * 0.8);
                starty = size.height / 2;
                //BaseTestScript.getDriver().swipe(startx, starty, endx, starty, 300);
                swipe(startx, starty, endx, starty, 300);

                break;
            case SLIDE_UP:
                startx = (int) (size.width * 0.5);
                endx = (int) (size.width * 0.5);
                starty = (int) (size.height * 0.6);
                endy = (int) (size.height * 0.4);
                //BaseTestScript.getDriver().swipe(startx, starty, endx, endy, 300);
                swipe(startx, starty, endx, endy, 300);
                break;
            case SLIDE_DOWN:
                startx = (int) (size.width * 0.25);
                endx = (int) (size.width * 0.52);
                starty = (int) (size.height * 0.4);
                endy = (int) (size.height * 0.6);
                //BaseTestScript.getDriver().swipe(startx, starty, endx, endy, 300);
                swipe(startx, starty, endx, endy, 300);
                break;
        }
    }

     /*
    Method to scroll screen Top to bottom
     */

    public void scrollToTopToBottom(int intNoOfScrolls) throws InterruptedException {

        Dimension dimensions = driver.manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.8;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.1;
        int scrollEnd = screenHeightEnd.intValue();
        //Here we use random number(35) for iteration
        for (int i = 0; i < intNoOfScrolls; i++) {
            //driver.swipe(0, scrollStart, 0, scrollEnd, 2000);

            swipe(0, scrollStart, 0, scrollEnd, 2000);
            Thread.sleep(2000);
        }
    }

    /*
    Method to scroll screen Top to bottom
     */

    public void scrollToBottomToTop(int intNoOfScrolls) throws InterruptedException {

        Dimension dimensions = driver.manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.3;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.8;
        int scrollEnd = screenHeightEnd.intValue();
        //Here we use random number(35) for iteration
        for (int i = 0; i < intNoOfScrolls; i++) {
            //driver.swipe(0, scrollStart, 0, scrollEnd, 2000);

            swipe(0, scrollStart, 0, scrollEnd, 2000);
        }
    }


    /**
     * Method to Scroll to the element
     *
     * @param control : Uicontrol object
     * @throws InterruptedException
     */
    public void swipeToElement(UiControl control) throws Exception {
        int waitTime = 500;
        long startTime = System.currentTimeMillis();
        while (!control.isFound(5)) {
            if (InitializerScript.isAndroid()) {
                waitTime = 300;
            }
            /*TouchAction action0 = (new TouchAction(InitializerScript.getDriver())).
                    press(88, 288).waitAction(waitTime).moveTo(88, 38).release();
            action0.perform();*/
            swipe(88, 288, 88, 38, waitTime);
            Thread.sleep(1000l);

            if ((System.currentTimeMillis() - startTime) > 60000) {
                break;
            }
        }


    }

    /**
     * Method to swipe element horizontally.
     */
    public void slideScreen(Direction direction) throws VerificationFailException {
        InitializerScript.getDriver().context("NATIVE_APP");
        Dimension size = InitializerScript.getDriver().manage().window().getSize();
        int startx = 0;
        int endx = 0;
        int starty = 0;
        switch (direction) {
            case SLIDE_LEFT:
                startx = (int) (size.width * 0.8);
                endx = (int) (size.width * 0.025);
                starty = size.height / 2;
                //InitializerScript.getDriver().swipe(startx, starty, endx, starty, 300);
                swipe(startx, starty, endx, starty, 300);
                break;
            case SLIDE_RIGHT:
                startx = (int) (size.width * 0.025);
                endx = (int) (size.width * 0.8);
                starty = size.height / 2;
                //InitializerScript.getDriver().swipe(startx, starty, endx, starty, 300);
                swipe(startx, starty, endx, starty, 300);
                break;
            case SLIDE_UP:
                startx = (int) (size.width * 0.62);
                endx = (int) (size.width * 0.38);
                starty = (int) (size.height / 1.14);
                int endy = size.height / 2;
                //InitializerScript.getDriver().swipe(startx, starty, endx, endy, 300);
                swipe(startx, starty, endx, endy, 300);
                break;
            case SLIDE_DOWN:
                startx = (int) (size.width * 0.38);
                endx = (int) (size.width * 0.62);
                starty = (int) (size.height / 2);
                endy = (int) (size.height / 1.14);
                //InitializerScript.getDriver().swipe(startx, starty, endx, endy, 300);
                swipe(startx, starty, endx, endy, 300);
                break;
        }
    }

    /**
     * Get control list from screen
     *
     * @param control
     * @return
     * @throws VerificationFailException
     * @throws InterruptedException
     */
    public List<String> getControlList(UiControl control) throws VerificationFailException, InterruptedException {
        List<String> Newlist = new ArrayList<String>();
        List<WebElement> list = control.findElements();
        String conName = "";
        while (!list.get(list.size() - 1).getText().equals(conName)) {
            WebElement el, e2;
            if (list.size() == 2) {
                el = list.get(0);
                e2 = list.get(1);
            } else {
                el = list.get(0);
                e2 = list.get(2);
            }

            for (WebElement element : list) {
                if (!Newlist.contains(element.getText())) {
                    Newlist.add(element.getText());
                }
            }
            conName = list.get(list.size() - 1).getText();
            /*TouchAction action0 = (new TouchAction(driver)).press(e2.getLocation().getX(), e2.getLocation().getY())
                    .waitAction(500).moveTo(el.getLocation().getX(), el.getLocation().getY()).release();
            action0.perform();*/
            swipe(e2.getLocation().getX(), e2.getLocation().getY(), el.getLocation().getX(), el.getLocation().getY(), 500);

            Thread.sleep(1000l);

            list = control.findElements();
        }
        return Newlist;
    }

    /**
     * Get control list from screen
     *
     * @param control
     * @return
     * @throws VerificationFailException
     * @throws InterruptedException
     */
    public List<String> getHorizontalControlList(UiControl control) throws VerificationFailException, InterruptedException {
        List<String> Newlist = new ArrayList<String>();
        List<WebElement> list = control.findElements();
        String conName = "";
        while (!list.get(list.size() - 1).getText().equals(conName)) {
            WebElement el = list.get(0), e2 = list.get(1);
            for (WebElement element : list) {
                if (!Newlist.contains(element.getText())) {
                    Newlist.add(element.getText());
                }
            }
            conName = list.get(list.size() - 1).getText();
            //TouchAction action0 = (new TouchAction(driver)).press(e2.getLocation().getX(), e2.getLocation().getY())
            //     .waitAction(500).moveTo(el.getLocation().getX(), el.getLocation().getY()).release();


            Dimension size = driver.manage().window().getSize();
            int startX = (int) (size.width * 0.80);
            int endX = (int) (size.width * 0.20);
            int startY = el.getLocation().getY();

             /*BaseTestScript.getDriver().swipe(startX, startY, endX, startY, 3000);

            TouchAction action0 = (new TouchAction(driver)).press(e2.getLocation().getX(), e2.getLocation().getY())
                    .waitAction(2000).moveTo(X1, el.getLocation().getY()).release();
            action0.perform();*/
            swipe(startX, startY, endX, startY, 3000);

            Thread.sleep(1000l);

            list = control.findElements();
        }
        return Newlist;
    }

    /**
     * Get control list from screen
     *
     * @param control
     * @return WebElement List
     * @throws VerificationFailException
     * @throws InterruptedException
     */
    public List<WebElement> getElementList(UiControl control) throws VerificationFailException, InterruptedException {
        List<WebElement> list = control.findElements();
        return list;
    }

    /**
     * Get control list from screen
     *
     * @param control
     * @return String List
     * @throws VerificationFailException
     * @throws InterruptedException
     */
    public List<String> getElementTextList(UiControl control) throws VerificationFailException, InterruptedException {
        List<String> Newlist = new ArrayList<String>();
        List<WebElement> list = control.findElements();
        for (WebElement element : list) {
            if (!Newlist.contains(element.getText())) {
                //The below line is to remove an extra space that is being added in the APK code.
                //Remove this once the issue is fixed
                String tempContact = element.getText();
                Newlist.add(tempContact);
            }
        }
        return Newlist;
    }

    public void scrollTo(String strLocator) {
        // driver.scrollTo(strLocator);
//*******This works too*********//
//        String selectorString = String.format("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text(\"" + strLocator + "\"))");
//        InitializerScript.getDriver().findElement(MobileBy.AndroidUIAutomator(selectorString));
//*******This works too*********//

        try {
//            MobileElement elementToClick = (MobileElement) androidDriver
//                    .findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().resourceId(\"horizontalTab\")).scrollIntoView(new UiSelector().text(\"CHILD(19 Months–6 Years)\"));");
//            elementToClick.click();
//
//
//            //wrking
//            MobileElement el = (MobileElement) androidDriver
//                    .findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"CHILD(19 Months–6 Years)\"));");
//            el.click();
//
//            //wrking
//            driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"CHILD(19 Months–6 Years)\"));")).click();


            try {
                InitializerScript.getDriver().findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"" + strLocator + "\"));"));
            } catch (Exception e) {
                System.out.println("We got an error scrolling!");
            }

        } catch (Exception e) {
            System.out.println("Cannot scroll further");
        }


        //androidDriver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + strLocator + "\").instance(0))");
    }

    //Scroll till element is visible in the downward direction
    public void scrollTillVisible(UiControl uiControl) {

        if (uiControl.findElements().size() == 0) {
            int exitPoint = 0;
            Dimension screenSize = InitializerScript.getDriver().manage().window().getSize();
            int startx = (int) (screenSize.width * 0.5);
            int endx = (int) (screenSize.width * 0.5);
            int starty = (int) (screenSize.height * 0.6);
            int endy = (int) (screenSize.height * 0.4);
            while (uiControl.findElements().size() == 0) {
                if (exitPoint < 5) {
                    swipe(startx, starty, endx, endy, 1000);
                    // BaseTestScript.getDriver().swipe(startx, starty, endx, endy, 1000);
                } else {
                    FrameworkLogger.logStep("Terminating swiping operation after 5 swipes");
                    break;
                }
                exitPoint++;
            }
            exitPoint = 0;
            if (uiControl.findElements().size() == 0) {
                FrameworkLogger.logStep("Checking in the opposite direction");
                while (uiControl.findElements().size() == 0) {
                    if (exitPoint < 5) {

                        swipe(startx, endy, endx, starty, 1000);
                        //BaseTestScript.getDriver().swipe(startx, endy, endx, starty, 1000);
                    } else {
                        FrameworkLogger.logStep("Terminating swiping operation after 5 swipes");
                        break;
                    }
                    exitPoint++;
                }
            }
        }
    }

    //Added for record screen swipe
    public void swipePdfForPageGrid(Direction direction) throws VerificationFailException {
        InitializerScript.getDriver().context("NATIVE_APP");
        Dimension size = InitializerScript.getDriver().manage().window().getSize();
        int startx = 0;
        int endx = 0;
        int starty = 0;
        int endy = 0;
        switch (direction) {
            case SLIDE_LEFT:
                startx = (int) (size.width * 0.8);
                endx = (int) (size.width * 0.025);
                starty = size.height / 2;
                swipe(startx, starty, endx, starty, 300);
//             BaseTestScript.getDriver().swipe(startx, starty, endx, starty, 300);
                break;
            case SLIDE_RIGHT:
                startx = (int) (size.width * 0.025);
                endx = (int) (size.width * 0.8);
                starty = size.height / 2;
                swipe(startx, starty, endx, starty, 300);
//              BaseTestScript.getDriver().swipe(startx, starty, endx, starty, 300);
                break;
            case SLIDE_UP:
                startx = (int) (size.width * 0.5);
                endx = (int) (size.width * 0.5);
                starty = (int) (size.height * 0.6);
                endy = (int) (size.height * 0.4);
                swipe(startx, starty, endx, endy, 300);
//              BaseTestScript.getDriver().swipe(startx, starty, endx, endy, 300);
                break;
            case SLIDE_DOWN:
                startx = (int) (size.width * 0.50);
                endx = (int) (size.width * 0.80);
                starty = (int) (size.height * 0.6);
                endy = (int) (size.height * 0.8);
                swipe(startx, starty, endx, endy, 300);
//            BaseTestScript.getDriver().swipe(startx, starty, endx, endy, 300);
                break;
        }
    }

    public void horizontalScroll(Direction direction) {

        //BaseTestScript.getDriver().context("NATIVE_APP");
        Dimension size = InitializerScript.getDriver().manage().window().getSize();
        int startx = 0;
        int endx = 0;
        int starty = 0;
        switch (direction) {
            case SLIDE_LEFT:
                startx = (int) (size.width * 0.8);
                endx = (int) (size.width * 0.20);
                starty = (int) (size.height * 0.20);
                //BaseTestScript.getDriver().swipe(startx, starty, endx, starty, 3000);
                swipe(startx, starty, endx, starty, 3000);
                break;
            case SLIDE_RIGHT:
                startx = (int) (size.width * 0.025);
                endx = (int) (size.width * 0.8);
                starty = (int) (size.height * 0.20);
                //BaseTestScript.getDriver().swipe(startx, starty, endx, starty, 3000);
                swipe(startx, starty, endx, starty, 3000);
                break;

        }
    }

    /*
    doubleTap(): Double taps on an element by x,y co-ordinate
     */
    public void doubleTap(WebElement element,int duration)  {

        Point p = element.getLocation();

        new TouchAction(driver)
                .press(point(p.getX(), p.getY()))
                .release()
                .perform()
                .press(point(p.getX(), p.getY()))
                .release()
                .perform();




    }


}
