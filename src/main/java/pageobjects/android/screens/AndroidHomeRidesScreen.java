package pageobjects.android.screens;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.HowToUseLocators;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.android.widgets.AndroidSideMenu;
import pageobjects.base.AbstractScreen;
import utils.TestReporter;

import static io.appium.java_client.pagefactory.LocatorGroupStrategy.ALL_POSSIBLE;

public class AndroidHomeRidesScreen extends AbstractScreen {

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath =
          "//*[@resource-id='android:id/content']")
  private MobileElement getContentScreenContainer;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath =
          "//*[@resource-id='com.hdw.james.rider:id/MAIN_MENU_ID']")
  private MobileElement sideMenuButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//*[@text='Rides']")
  private MobileElement rideTitle;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']")
  private MobileElement sideMenuContainer;

  /***
   * <p>Constructor of the home rides Screen.</p>
   */
  public AndroidHomeRidesScreen() {
    super();
    logger.debug("Initilizing home rides Screen screen");
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    isLoaded(getContentScreenContainer);
  }

  /**
   * Clicks on continue button.
   */
  public AndroidSideMenu openSideMenu() {
    logger.debug("Clicks on continue button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(sideMenuButton));
    sideMenuButton.click();
    AndroidSideMenu androidSideMenu = new AndroidSideMenu(sideMenuContainer);
    return androidSideMenu;
  }

  /**
   * Verifies if the rides title is present on the home results.
   *
   * @return true if present, false otherwise.
   */
  public boolean isRidesTitleDisplayed() {
    TestReporter.addInfoToReport("Check if the rides title is present in the home page");
    try {
      WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
      wait.until(ExpectedConditions.visibilityOf(rideTitle));
      return rideTitle.isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("The home page does not have the rides title", e);
      return false;
    }
  }
}
