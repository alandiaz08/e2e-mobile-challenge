package pageobjects.base;

import appiumdriver.AppiumDriverBase;
import io.appium.java_client.*;
import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;

public abstract class AbstractWidget extends Widget {

  /**
   * Logger.
   */
  protected static final Logger logger = LogManager.getLogger(AbstractWidget.class);
  protected AppiumDriver<MobileElement> driver;

  protected static final int WAIT_TIMEOUT = 30;

  public AbstractWidget(MobileElement container) {
    super(container);
    driver = AppiumDriverBase.getDriver();
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

