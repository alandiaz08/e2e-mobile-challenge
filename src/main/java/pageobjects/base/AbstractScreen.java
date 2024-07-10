package pageobjects.base;

import appiumdriver.AppiumDriverBase;
import customexceptions.PageObjectLoadingError;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestReporter;

public abstract class AbstractScreen {

  /**
   * Logger.
   */
  protected static final Logger logger = LogManager.getLogger(AbstractScreen.class);

  protected AppiumDriver<MobileElement> driver;

  protected static final int WAIT_TIMEOUT = 10;
  protected static final int WAIT_HORIZONTAL_SWIPE = 1500;


  private static final int HALF = 2;
  private static final double ANCHOR_PERCENTAGE = 0.5;
  private static final double START_POINT_PERCENTAGE = 0.5;
  private static final double NEW_START_POINT_PERCENTAGE = 0.8;
  private static final double START_POINT_PERCENTAGE_LONG = 0.95;
  private static final double END_POINT_PERCENTAGE = 0.1;

  private static final int TIMEOUT = 1500;
  private static final int HORIZONTAL_TIMEOUT = 500;
  protected static final int WAIT_SCROLL = 2;
  protected static final int WAIT = 2;

  public AbstractScreen() {
    driver = AppiumDriverBase.getDriver();
  }

  /**
   * Tap centre of the screen on either android or ios.
   */
  protected void tapCentreOfTheScreen() {
    logger.debug("Tap the center of the screen on android");
    Dimension windowSize = AppiumDriverBase.getDriver().manage().window().getSize();
    TouchAction touchAction = new TouchAction(AppiumDriverBase.getDriver());
    int centerPointX = windowSize.getWidth() / HALF;
    int centerPointY = windowSize.getHeight() / HALF;
    logger.debug("Tap the center of the screen at point ({}, {})", centerPointX, centerPointY);
    touchAction.tap(PointOption.point(centerPointX, centerPointY)).perform();
  }

  /**
   * Short scroll down android.
   */
  protected void scrollDown() {
    logger.debug("Scroll down");
    Dimension size = AppiumDriverBase.getDriver().manage().window().getSize();
    int anchor = (int) (size.width * ANCHOR_PERCENTAGE);
    int startPoint = (int) (size.height * START_POINT_PERCENTAGE);
    int endPoint = (int) (size.height * END_POINT_PERCENTAGE);
    TouchAction touchAction = new TouchAction(driver);
    touchAction.press(PointOption.point(anchor, startPoint))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(TIMEOUT)))
            .moveTo(PointOption.point(anchor, endPoint)).release().perform();
  }

  /**
   * Long scroll down android.
   */
  private void longScroll() {
    logger.debug("Scroll down");
    Dimension size = AppiumDriverBase.getDriver().manage().window().getSize();
    int anchor = (int) (size.width * ANCHOR_PERCENTAGE);
    int startPoint = (int) (size.height * START_POINT_PERCENTAGE_LONG);
    int endPoint = (int) (size.height * END_POINT_PERCENTAGE);
    TouchAction touchAction = new TouchAction(driver);
    touchAction.press(PointOption.point(anchor, startPoint))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(TIMEOUT)))
            .moveTo(PointOption.point(anchor, endPoint)).release().perform();
  }

  /**
   * Short Scroll down vertically to element.
   *
   * @param selector element to scroll to
   * @param maxTries number of retries
   */
  protected void shortScrollToAnElement(By selector, int maxTries) {
    logger.debug("Scroll down to element {}", selector);
    WebDriverWait waitScroll = new WebDriverWait(driver, WAIT_SCROLL);
    int scrollAttempt = 0;
    while (scrollAttempt < maxTries) {
      try {
        logger.debug("Attempt {} to find element", scrollAttempt);
        waitScroll.until(ExpectedConditions.visibilityOfElementLocated(selector));
        break;
      } catch (Exception e) {
        logger.debug("Element not found in the screen, scroll to find", e);
        scrollDown();
        scrollAttempt++;
      }
    }
    if (scrollAttempt >= maxTries) {
      logger.debug("Last attempt to find the element");
      driver.findElement(selector);
    }
  }

  /**
   * Long Scroll down vertically to element.
   *
   * @param selector element to scroll to
   * @param maxTries number of retries
   */
  protected void longScrollToAnElement(By selector, int maxTries) {
    logger.debug("Scroll down to element {}", selector);
    int scrollAttempt = 0;
    WebDriverWait waitScroll = new WebDriverWait(driver, WAIT_SCROLL);
    while (scrollAttempt < maxTries) {
      try {
        logger.debug("Attempt {} to find element", scrollAttempt);
        waitScroll.until(ExpectedConditions.visibilityOfElementLocated(selector));
        break;
      } catch (Exception e) {
        logger.debug("Element not found in the screen, scroll to find", e);
        longScroll();
        scrollAttempt++;
      }
    }
    if (scrollAttempt >= maxTries) {
      logger.debug("Last attempt to find the element");
      driver.findElement(selector);
    }
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
   * Swipe horizontal to element.
   *
   * @param element element to scroll to
   */
  protected void horizontalSwipeToAnElementInList(By element) {
    logger.debug("Get location of element you want to swipe");
    List<MobileElement> elementList = driver.findElements(element);
    int finalElement = elementList.size() - 1;
    final Point bannerPoint = elementList.get(finalElement).getLocation();

    logger.debug("Get size of device screen");
    Dimension screenSize = driver.manage().window().getSize();

    logger.debug("Get start and end coordinates for horizontal swipe");
    int startX = Math.toIntExact(Math.round(screenSize.getWidth() * START_POINT_PERCENTAGE));
    int endX = 0;

    TouchAction action = new TouchAction(driver);

    logger.debug("Start the horizontal swipe");
    action
            .press(PointOption.point(startX, bannerPoint.getY()))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(WAIT_HORIZONTAL_SWIPE)))
            .moveTo(PointOption.point(endX, bannerPoint.getY()))
            .release();
    driver.performTouchAction(action);
  }

  /**
   * Swipe right on an element.
   *
   * @param element element to scroll to.
   */
  protected void swipeRightOnAnElement(By element) {
    logger.debug("Get location of element you want to swipe");
    final Point bannerPoint = driver.findElement(element).getLocation();

    logger.debug("Get size of device");
    Dimension screenSize = driver.manage().window().getSize();

    logger.debug("Get start and end coordinates for horizontal swipe");
    int startPointX = (int) (screenSize.width * END_POINT_PERCENTAGE);
    int endPointX = (int) (screenSize.width * NEW_START_POINT_PERCENTAGE);

    TouchAction action = new TouchAction(driver);

    logger.debug("Start the horizontal swipe");
    action
            .press(PointOption.point(startPointX, bannerPoint.getY()))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(WAIT_HORIZONTAL_SWIPE)))
            .moveTo(PointOption.point(endPointX, bannerPoint.getY()))
            .release();
    driver.performTouchAction(action);
  }

  /**
   * Implicit wait.
   *
   * @param milliseconds milliseconds to wait.
   */
  protected void implicitWait(int milliseconds) {
    logger.debug("Wait for 3 seconds...");
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      logger.error(e);
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Logs and throws the exception caught when loading the screen/widget.
   *
   * @param customMessage The custom message to write in the log.
   * @param e Exception caught when trying to load the component.
   */
  protected void throwNotLoadedException(String customMessage, @NotNull Exception e) {
    throw new PageObjectLoadingError(customMessage + "\n" + e.getMessage(), e.getCause());
  }

  /**
   * Method to check if a screen is ready to be operated.
   *
   * @param element The custom message to write in the log.
   */
  public void isLoaded(MobileElement element) {
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    try {
      final Instant start = Instant.now();
      wait.until(ExpectedConditions.visibilityOf(element));
      final Instant finish = Instant.now();
      final long timeElapsed = Duration.between(start, finish).toMillis();
      TestReporter.addScreenshotToReport("Screen load time " + timeElapsed + "milliseconds");
    } catch (Exception e) {
      throwNotLoadedException("Screen not loaded exception", e);
    }
  }

  /**
   * Method to check if a screen is ready to be operated.
   *
   * @param elementBy The custom message to write in the log.
   */
  public void isLoadedBy(By elementBy) {
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    try {
      final Instant start = Instant.now();
      wait.until(ExpectedConditions.presenceOfElementLocated(elementBy));
      final Instant finish = Instant.now();
      final long timeElapsed = Duration.between(start, finish).toMillis();
      TestReporter.addScreenshotToReport("Screen load time " + timeElapsed + "milliseconds");
    } catch (Exception e) {
      throwNotLoadedException("Screen not loaded exception", e);
    }
  }
}

