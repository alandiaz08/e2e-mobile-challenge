package pageobjects.android.widgets;

import appiumdriver.AppiumDriverBase;
import com.neovisionaries.i18n.CountryCode;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.util.List;

import io.appium.java_client.pagefactory.HowToUseLocators;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.base.AbstractWidget;
import utils.TestReporter;

import static io.appium.java_client.pagefactory.LocatorGroupStrategy.ALL_POSSIBLE;

public class AndroidPhoneNumberWidget extends AbstractWidget {

  protected static final Logger logger = LogManager.getLogger(AndroidPhoneNumberWidget.class);

  //selectors
  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id='com.hdw.james.rider:id/input']")
  private MobileElement phoneNumberField;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//*[@resource-id='com.hdw.james.rider:id/spinner']")
  private MobileElement countryCodeButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//*[@resource-id='com.hdw.james.rider:id/continueButton']")
  private MobileElement continueButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.hdw.james.rider:id/title']")
  private MobileElement title;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.hdw.james.rider:id/description']")
  private MobileElement description;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']")
  private MobileElement validateCodeNumberContainer;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.ListView")
  private MobileElement dropdownContainer;

  private static final By dropdownContainerBy = new ByAll(
          By.xpath("//android.widget.ListView"));
  private static final By dropDownItemsBy = new ByAll(
          By.xpath("//*[@resource-id='com.hdw.james.rider:id/text']"));

  private static final int WAIT_TIMEOUT = 5;


  /**
   * Initializes the phone number widget with the container.
   *
   * @param container the element that contains the widget.
   */
  public AndroidPhoneNumberWidget(MobileElement container) {
    super(container);
    PageFactory.initElements(new AppiumFieldDecorator(container), this);
    TestReporter.addInfoToReport("Initialising the AndroidPhoneNumber widget");
    driver.hideKeyboard();
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(phoneNumberField));
    TestReporter.addScreenshotToReport("Android phone number widget is loaded successfully");
  }

  /**
   * Sets country code.
   *
   * @param countryCode CountryCode
   * @return AndroidPhoneNumberWidget
   */
  public AndroidPhoneNumberWidget setCountryCode(CountryCode countryCode, String areaCode) {
    TestReporter.addInfoToReport("Set country code on AndroidPhoneNumber component");
    countryCodeButton.click();
    WebDriverWait wait = new WebDriverWait(AppiumDriverBase.getDriver(), WAIT_TIMEOUT);

    boolean countryCodeFound = false;
    int maxScrollAttempts = 24;  // Número máximo de intentos de desplazamiento
    int currentScrollAttempt = 0;

    while (!countryCodeFound && currentScrollAttempt < maxScrollAttempts) {
      WebElement listContainer = wait.until(ExpectedConditions.presenceOfElementLocated(dropdownContainerBy));
      List<WebElement> values = listContainer.findElements(dropDownItemsBy);

      for (WebElement value : values) {
        if (value.getText().contains(countryCode.getName() + " (" + areaCode + ")")) {
          value.click();
          countryCodeFound = true;
          break;
        }
      }

      if (!countryCodeFound) {
        scrollUp();
        currentScrollAttempt++;
      }
    }
    return this;
  }

  /**
   * Sets phone number by given string.
   *
   * @param phoneNumber String
   */
  public AndroidPhoneNumberWidget setPhoneNumber(String phoneNumber) {
    TestReporter.addInfoToReport("Setting phone number as: " + phoneNumber);
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(phoneNumberField));
    phoneNumberField.clear();
    phoneNumberField.sendKeys(phoneNumber);
    return this;
  }

  /**
   * Clicks on continue button.
   */
  public AndroidValidateCodeNumberWidget clickContinueButton() {
    TestReporter.addInfoToReport("Clicks on continue button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.elementToBeClickable(continueButton));
    continueButton.click();
    AndroidValidateCodeNumberWidget androidValidateCodeNumberWidget =
            new AndroidValidateCodeNumberWidget(validateCodeNumberContainer);
    return androidValidateCodeNumberWidget;
  }

  /**
   * Verifies if the title is displayed.
   *
   * @return true if the title is displayed, false otherwise.
   */
  public boolean isTitleDisplayed() {
    TestReporter.addInfoToReport("Checking if the title is displayed");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    try {
      wait.until(ExpectedConditions.visibilityOf(title));
      return title.isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("Title not displayed", e);
      return false;
    }
  }

  /**
   * Gets the text of the title.
   *
   * @return the text of the title.
   */
  public String getTitleText() {
    TestReporter.addInfoToReport("Getting the text of the title");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(title));
    String titleText = title.getText();
    TestReporter.addInfoToReport("Title text: " + titleText);
    return titleText;
  }

  /**
   * Verifies if the description is displayed.
   *
   * @return true if the description is displayed, false otherwise.
   */
  public boolean isDescriptionDisplayed() {
    TestReporter.addInfoToReport("Checking if the description is displayed");
    try {
      WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
      wait.until(ExpectedConditions.visibilityOf(description));
      return description.isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("Description not displayed");
      return false;
    }
  }

  /**
   * Gets the text of the description.
   *
   * @return the text of the description.
   */
  public String getDescriptionText() {
    TestReporter.addInfoToReport("Getting the text of the description");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(description));
    String descriptionText = description.getText();
    logger.info("Description text: " + descriptionText);
    return descriptionText;
  }

}
