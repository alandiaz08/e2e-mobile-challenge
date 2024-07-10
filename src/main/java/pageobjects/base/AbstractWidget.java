package pageobjects.base;

import appiumdriver.AppiumDriverBase;
import io.appium.java_client.*;
import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import java.time.Duration;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofSeconds;

public abstract class AbstractWidget extends Widget {

  /**
   * Logger.
   */
  protected static final Logger logger = LogManager.getLogger(AbstractWidget.class);
  protected AppiumDriver<MobileElement> driver;

  protected static final int WAIT_TIMEOUT = 30;
  protected static final int WAIT_HORIZONTAL_SWIPE = 3000;

  private static final double ANCHOR_PERCENTAGE = 0.5;
  private static final double START_POINT_PERCENTAGE = 0.5;
  private static final double END_POINT_PERCENTAGE = 0.1;
  private static final int TIMEOUT = 2000;

  public AbstractWidget(MobileElement container) {
    super(container);
    driver = AppiumDriverBase.getDriver();
  }

  /**
   * Scroll vertical.
   */
  protected void scrollDown() {
    logger.debug("Scroll down vertically on the widget");
    Dimension widgetSize = getWrappedElement().getSize();
    Point widgetLocation = getWrappedElement().getLocation();

    int anchor = (int) ((widgetSize.width + widgetLocation.x) * ANCHOR_PERCENTAGE);
    int startPoint = (int) (widgetSize.height * START_POINT_PERCENTAGE + widgetLocation.y);
    int endPoint = (int) (widgetSize.height * END_POINT_PERCENTAGE + widgetLocation.y);

    TouchAction touchAction = new TouchAction(driver);
    touchAction.press(point(anchor, startPoint))
            .waitAction(waitOptions(Duration.ofMillis(TIMEOUT)))
            .moveTo(point(anchor, endPoint)).release().perform();
  }

  /**
   * Swipe horizontal to element.
   *
   * @param element element to scroll to
   */
  protected void horizontalSwipeToAnElementInList(By element) {
    logger.debug("Getting location of the element you want to swipe");
    List<MobileElement> mobileElements = driver.findElements(element);
    int finalElement = mobileElements.size() - 1;
    final Point bannerPoint = mobileElements.get(finalElement).getLocation();

    logger.debug("Getting size of device screen");
    Dimension screenSize = driver.manage().window().getSize();

    logger.debug("Screen size: {}", screenSize);

    logger.debug("Getting start and end coordinates for horizontal swipe");
    int startX = Math.toIntExact(Math.round(screenSize.getWidth() * START_POINT_PERCENTAGE));
    int endX = 0;

    TouchAction touchAction = new TouchAction(driver);

    logger.debug("Starting the horizontal swipe");
    touchAction
            .press(point(startX, bannerPoint.getY()))
            .waitAction(waitOptions(Duration.ofMillis(WAIT_HORIZONTAL_SWIPE)))
            .moveTo(point(endX, bannerPoint.getY()))
            .release();
    driver.performTouchAction(touchAction);
  }

  /**
   * scroll in android based on text.
   *
   * @param text Text to look for.
   */
  protected void uiAutomatorScrollWithText(String text) {
    logger.debug("Scroll in android based on text");
    driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable"
            + "(new UiSelector()).scrollIntoView(new UiSelector().text(\"" + text
            + "\"));"));
  }

  /**
   * Scroll up android.
   */
  protected void scrollUpGood() {
    logger.debug("Scroll up");
    Dimension size = AppiumDriverBase.getDriver().manage().window().getSize();
    int anchor = (int) (size.width * ANCHOR_PERCENTAGE);
    int startPoint = (int) (size.height * END_POINT_PERCENTAGE);
    int endPoint = (int) (size.height * START_POINT_PERCENTAGE);
    TouchAction touchAction = new TouchAction(driver);
    touchAction.press(point(anchor, startPoint))
            .waitAction(waitOptions(Duration.ofMillis(TIMEOUT)))
            .moveTo(point(anchor, endPoint)).release().perform();
  }

  /**
   * Performs a scroll up action on the screen.
   */
  protected void scrollUp() {
    Dimension size = driver.manage().window().getSize();
    logger.debug(size);

    // calculate coordinates for vertical swipe
    int startVerticalY = (int) (size.height * 0.3);
    int endVerticalY = (int) (size.height * 0.8);
    int startVerticalX = (int) (size.width / 2);

    new TouchAction<>(driver)
            .press(PointOption.point(startVerticalX, startVerticalY))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
            .moveTo(PointOption.point(startVerticalX, endVerticalY))
            .release()
            .perform();
  }


}

