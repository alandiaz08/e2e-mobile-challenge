package pageobjects.android.screens;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.HowToUseLocators;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.android.widgets.AndroidPhoneNumberWidget;
import pageobjects.base.AbstractScreen;

import static io.appium.java_client.pagefactory.LocatorGroupStrategy.ALL_POSSIBLE;

public class AndroidOnboardingScreen extends AbstractScreen {

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath =
          "//*[@resource-id='com.hdw.james.rider:id/activitySingleNavFragment']")
  private MobileElement getStartedScreenContainer;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath =
          "//*[@resource-id='com.hdw.james.rider:id/getStartedButton']")
  private MobileElement getStartedButton;

  @HowToUseLocators(androidAutomation = ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//*[@resource-id='android:id/content']")
  private MobileElement phoneNumberWidget;

  /***
   * <p>Constructor of the Onboarding Screen.</p>
   */
  public AndroidOnboardingScreen() {
    super();
    logger.debug("Initilizing onboarding screen");
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    isLoaded(getStartedScreenContainer);
  }

  /***
   * <p>Clicks the get started button.</p>
   */
  public AndroidPhoneNumberWidget getStarted() {
    logger.debug("Get started to onboarding");
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    wait.until(ExpectedConditions.visibilityOf(getStartedButton));
    logger.debug("Click the get started button");
    getStartedButton.click();
    AndroidPhoneNumberWidget androidPhoneNumberWidget = new AndroidPhoneNumberWidget(phoneNumberWidget);
    return androidPhoneNumberWidget;
  }
}
