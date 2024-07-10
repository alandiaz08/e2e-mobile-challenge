package pageobjects.android.widgets;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.HowToUseLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.android.screens.AndroidHomeRidesScreen;
import pageobjects.base.AbstractWidget;
import utils.TestReporter;

import static io.appium.java_client.pagefactory.LocatorGroupStrategy.ALL_POSSIBLE;

public class AndroidPermissionsRequestWidget extends AbstractWidget {

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.hdw.james.rider:id/permissionsTextTitle']")
  private MobileElement permissionRequestTitle;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.Button[@resource-id='com.hdw.james.rider:id/permissionsContinueButton']")
  private MobileElement continueButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.Button[@resource-id='com.hdw.james.rider:id/permissionsLocationButton']")
  private MobileElement allowLocationButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.Button[@resource-id='com.hdw.james.rider:id/permissionsNotificationButton']")
  private MobileElement allowNotificationsButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.Button"
          + "[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']")
  private MobileElement allowWhileUsingTheAppButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.Button"
          + "[@resource-id='com.android.permissioncontroller:id/permission_allow_button']")
  private MobileElement allowSendNotificationsButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.LinearLayout"
          + "[@resource-id='com.android.permissioncontroller:id/grant_dialog']")
  private MobileElement allowPopUpContainer;

  private static final By allowPopUpContainerBy = new ByAll(
          By.xpath("//android.widget.LinearLayout"
                  + "[@resource-id='com.android.permissioncontroller:id/grant_dialog']"));


  /**
   * Initializes the permissions request widget with the container.
   *
   * @param container the element that contains the widget.
   */
  public AndroidPermissionsRequestWidget(MobileElement container) {
    super(container);
    PageFactory.initElements(new AppiumFieldDecorator(container), this);
    logger.debug("Initialising the PermissionsRequest widget");
    driver.hideKeyboard();
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(continueButton));
    TestReporter.addScreenshotToReport("Android permissions request widget is loaded successfully");
  }

  /**
   * Clicks on continue button.
   */
  public AndroidHomeRidesScreen allowAndContinueToHomeRidesScreen() {
    logger.debug("Clicks on continue button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(continueButton));
    allowPermissionsRequests();
    continueButton.click();
    AndroidHomeRidesScreen androidHomeRidesScreen = new AndroidHomeRidesScreen();
    return androidHomeRidesScreen;
  }

  /**
   * Clicks on allow location button.
   */
  public AndroidPermissionsRequestWidget allowLocation() {
    logger.debug("Clicks on allow location button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(allowLocationButton)).click();
    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(allowPopUpContainerBy));
    if (allowPopUpContainer.isDisplayed()) {
      wait.until(ExpectedConditions.elementToBeClickable(allowWhileUsingTheAppButton));
      allowWhileUsingTheAppButton.click();
    }
    return this;
  }

  /**
   * Clicks on allow notifications button.
   */
  public AndroidPermissionsRequestWidget allowNotifications() {
    logger.debug("Clicks on allow notifications button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(allowNotificationsButton)).click();
    wait.until(ExpectedConditions.presenceOfElementLocated(allowPopUpContainerBy));
    if (allowPopUpContainer.isDisplayed()) {
      wait.until(ExpectedConditions.elementToBeClickable(allowSendNotificationsButton));
      allowSendNotificationsButton.click();
    }
    return this;
  }

  /**
   * Allow permissions requests.
   */
  public AndroidPermissionsRequestWidget allowPermissionsRequests() {
    logger.debug("Allow permissions requests");
    allowLocation();
    allowNotifications();
    return this;
  }
}
