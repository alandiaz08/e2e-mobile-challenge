package pageobjects.android.widgets;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.HowToUseLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.android.screens.AndroidHomeRidesScreen;
import pageobjects.base.AbstractWidget;
import utils.TestReporter;

import java.util.List;

import static io.appium.java_client.pagefactory.LocatorGroupStrategy.ALL_POSSIBLE;

public class AndroidValidateCodeNumberWidget extends AbstractWidget {

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.hdw.james.rider:id/title']")
  private MobileElement validateCodeNumberTitle;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.Button[@resource-id='com.hdw.james.rider:id/continueButton']")
  private MobileElement continueButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']")
  private MobileElement permissionsRequestContainer;

  private static final By inputFieldsBy = new ByAll(
          By.xpath("//android.widget.EditText[@resource-id='com.hdw.james.rider:id/inputEditText']"));

  /**
   * Initializes validate code number widget with the container.
   *
   * @param container the element that contains the widget.
   */
  public AndroidValidateCodeNumberWidget(MobileElement container) {
    super(container);
    PageFactory.initElements(new AppiumFieldDecorator(container), this);
    TestReporter.addInfoToReport("Initialising the AndroidValidateCodeNumber widget");
    driver.hideKeyboard();
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(validateCodeNumberTitle));
    TestReporter.addScreenshotToReport("Android validate code number widget is loaded successfully");
  }

  /**
   * Enters each character of the provided value into the corresponding input fields.
   *
   * @param value The value to be entered, where each character will be sent to an input field.
   * @throws IllegalArgumentException if the length of the value exceeds the number of input fields.
   */
  public AndroidValidateCodeNumberWidget enterValueInEachField(String value) {
    TestReporter.addInfoToReport("Starting to enter value in each input field");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(inputFieldsBy));
    List<MobileElement> inputFieldsList = driver.findElements(inputFieldsBy);
    if (value.length() > inputFieldsList.size()) {
      logger.error("The entered value is longer than the number of available text fields.");
      throw new IllegalArgumentException("The entered value is longer than the number of available text fields.");
    }
    for (int i = 0; i < value.length(); i++) {
      WebElement inputField = inputFieldsList.get(i);
      char charToSend = value.charAt(i);
      logger.debug("Sending character '{}' to the text field at position {}", charToSend, i);
      inputField.sendKeys(Character.toString(charToSend));
    }
    logger.debug("Value entered successfully in all text fields");
    return this;
  }

  /**
   * Clicks on continue button.
   */
  public AndroidHomeRidesScreen acceptAndGoToHomeRidesPage() {
    TestReporter.addInfoToReport("Clicks on continue button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(continueButton));
    continueButton.click();
    AndroidHomeRidesScreen androidHomeRidesScreen = new AndroidHomeRidesScreen();
    return androidHomeRidesScreen;
  }

  /**
   * Clicks on continue button.
   */
  public AndroidPermissionsRequestWidget acceptAndGoToPermissionsRequestWidget() {
    TestReporter.addInfoToReport("Clicks on continue button");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(continueButton));
    continueButton.click();
    AndroidPermissionsRequestWidget permissionsRequestWidget =
            new AndroidPermissionsRequestWidget(permissionsRequestContainer);
    return permissionsRequestWidget;
  }

}
