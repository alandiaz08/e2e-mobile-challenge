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

public class AndroidProfileWidget extends AbstractWidget {

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.ImageView[@resource-id='com.hdw.james.rider:id/profileImageView']")
  private MobileElement picture;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id='com.hdw.james.rider:id/firstNameInput']")
  private MobileElement firstNameField;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id='com.hdw.james.rider:id/lastNameInput']")
  private MobileElement lastNameField;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.hdw.james.rider:id/DEFAULT_TEXT_ACTION_MENU_ID']")
  private MobileElement doneButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']")
  private MobileElement sideMenuContainer;

  /**
   * Initializes the side menu with the container.
   *
   * @param container the element that contains the widget.
   */
  public AndroidProfileWidget(MobileElement container) {
    super(container);
    PageFactory.initElements(new AppiumFieldDecorator(container), this);
    logger.debug("Initialising the Side menu");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(picture));
    TestReporter.addScreenshotToReport("Android side menu widget is loaded successfully");
  }

  /**
   * Verifies if the profile picture is displayed.
   *
   * @return true if the profile picture is displayed, false otherwise.
   */
  public boolean isPictureDisplayed() {
    logger.debug("Checking if the profile picture is displayed");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    try {
      wait.until(ExpectedConditions.visibilityOf(picture));
      return picture.isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("Profile picture not displayed", e);
      return false;
    }
  }

  /**
   * Enters the first name in the first name field.
   *
   * @param firstName The first name to enter.
   */
  public AndroidProfileWidget enterFirstName(String firstName) {
    logger.debug("Entering first name: " + firstName);
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(firstNameField));
    firstNameField.clear();
    firstNameField.sendKeys(firstName);
    return this;
  }

  /**
   * Enters the last name in the last name field.
   *
   * @param lastName The last name to enter.
   */
  public AndroidProfileWidget enterLastName(String lastName) {
    logger.debug("Entering last name: " + lastName);
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(lastNameField));
    lastNameField.clear();
    lastNameField.sendKeys(lastName);
    return this;
  }

  /**
   * Clicks the done button.
   */
  public AndroidSideMenu clickDoneButton() {
    logger.debug("Clicking the done button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.elementToBeClickable(doneButton));
    doneButton.click();
    logger.info("Done button clicked");
    AndroidSideMenu androidSideMenu = new AndroidSideMenu(sideMenuContainer);
    return androidSideMenu;
  }

}
