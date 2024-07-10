package pageobjects.android.widgets;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.HowToUseLocators;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.base.AbstractWidget;
import utils.TestReporter;

import static io.appium.java_client.pagefactory.LocatorGroupStrategy.ALL_POSSIBLE;

public class AndroidSideMenu extends AbstractWidget {

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.FrameLayout"
          + "[@resource-id='com.hdw.james.rider:id/profileContainer']/android.view.ViewGroup")
  private MobileElement profileButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.hdw.james.rider:id/profileName']")
  private MobileElement profileName;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView"
          + "[@resource-id='com.hdw.james.rider:id/actionList']/android.view.ViewGroup[5]")
  private MobileElement logoutButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView"
          + "[@resource-id='com.hdw.james.rider:id/actionList']/android.view.ViewGroup[5]")
  private MobileElement profileUpdatedSuccessfullyPopup;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']")
  private MobileElement profileContainer;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//*[@resource-id='android:id/content']")
  private MobileElement phoneNumberWidget;

  private final WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);


  /**
   * Initializes the side menu with the container.
   *
   * @param container the element that contains the widget.
   */
  public AndroidSideMenu(MobileElement container) {
    super(container);
    PageFactory.initElements(new AppiumFieldDecorator(container), this);
    logger.debug("Initialising the Side menu");
    wait.until(ExpectedConditions.visibilityOf(profileName));
    TestReporter.addScreenshotToReport("Android side menu widget is loaded successfully");
  }

  /**
   * Clicks on continue button.
   */
  public AndroidProfileWidget openProfile() {
    logger.debug("Clicks on profile button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.elementToBeClickable(profileButton));
    profileButton.click();
    AndroidProfileWidget AndroidProfileWidget = new AndroidProfileWidget(profileContainer);
    return AndroidProfileWidget;
  }

  /**
   * Get the text of the profile name.
   *
   * @return The text of the profile name.
   */
  public String getProfileName() {
    logger.debug("Getting the text of the profile name");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(profileName));
    return profileName.getText();
  }

  /**
   * Clicks the logout button.
   */
  public AndroidPhoneNumberWidget logout() {
    logger.debug("Clicking the logout button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
    logoutButton.click();
    logger.info("Logout button clicked");
    AndroidPhoneNumberWidget androidPhoneNumberWidget = new AndroidPhoneNumberWidget(phoneNumberWidget);
    return androidPhoneNumberWidget;
  }

  /**
   * Verifies if the profile update was successful by checking the presence of the success popup.
   *
   * @return true if the profile update success popup is displayed, false otherwise.
   */
  public boolean isProfileUpdatedSuccessfully() {
    logger.debug("Checking if the profile update success popup is displayed");
    try {
      wait.until(ExpectedConditions.visibilityOf(profileUpdatedSuccessfullyPopup));
      return profileUpdatedSuccessfullyPopup.isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("Profile update success popup not displayed", e);
      return false;
    }
  }

}
