package android;

import base.TestBase;
import com.neovisionaries.i18n.CountryCode;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.android.screens.AndroidHomeRidesScreen;
import pageobjects.android.screens.AndroidOnboardingScreen;
import pageobjects.android.widgets.AndroidPhoneNumberWidget;
import pageobjects.android.widgets.AndroidSideMenu;
import utils.RandomStringGenerator;
import utils.TestReporter;

import java.security.SecureRandom;

@Test(groups = {"full-regression"})
public class DemoTests extends TestBase {

  private static final String PHONE_NUMBER = "701111112";
  private static final String SMS_CODE = "123456";

  private static final int MAX_LENGTH_NAME = 8;

  private final RandomStringGenerator nameGenerator = new RandomStringGenerator(MAX_LENGTH_NAME,
          new SecureRandom(), RandomStringGenerator.LOWERCASE);
  @Test(
     groups = {"smoke"},
     enabled = true,
     retryAnalyzer = TestBase.RetryAnalyzer.class)
  public void successfullyLogin() {

    //Arrange
    final String countryCode = "+93";

    //Act
    TestReporter.addInfoToReport("Launch a basic login");

    AndroidOnboardingScreen androidOnboardingScreen = new AndroidOnboardingScreen();

    AndroidHomeRidesScreen androidSearchScreen = androidOnboardingScreen
            .getStarted()
            .setCountryCode(CountryCode.AF, countryCode)
            .setPhoneNumber(PHONE_NUMBER)
            .clickContinueButton()
            .enterValueInEachField(SMS_CODE)
            .acceptAndGoToPermissionsRequestWidget()
            .allowAndContinueToHomeRidesScreen();

    //Assert
    TestReporter.addInfoToReport("Assert if the rides title is present in the home page");
    Assert.assertTrue(androidSearchScreen.isRidesTitleDisplayed(), "The rides title is not present"
            + " in the home page");
  }

  @Test(
     groups = {"smoke"},
     enabled = true,
     retryAnalyzer = TestBase.RetryAnalyzer.class)

  public void updateProfileAccount() {

    //Arrange
    final String countryCode = "+93";
    final String randomFirstName = nameGenerator.nextString();
    final String randomLastName = nameGenerator.nextString();

    //Act
    TestReporter.addInfoToReport("Launch a basic login");

    AndroidOnboardingScreen androidOnboardingScreen = new AndroidOnboardingScreen();

    AndroidSideMenu androidSideMenu = androidOnboardingScreen
            .getStarted()
            .setCountryCode(CountryCode.AF, countryCode)
            .setPhoneNumber(PHONE_NUMBER)
            .clickContinueButton()
            .enterValueInEachField(SMS_CODE)
            .acceptAndGoToPermissionsRequestWidget()
            .allowAndContinueToHomeRidesScreen()
            .openSideMenu()
            .openProfile()
            .enterFirstName(randomFirstName)
            .enterLastName(randomLastName)
            .clickDoneButton();

    //Assert
    TestReporter.addInfoToReport("Assert if profile update success popup is present in the side menu");
    Assert.assertTrue(androidSideMenu.isProfileUpdatedSuccessfully(), "The profile update success popup is "
            + "not present in the side menu");
  }

  @Test(
     groups = {"smoke"},
     enabled = true,
     retryAnalyzer = TestBase.RetryAnalyzer.class)

  public void successfullyLogout() {

    //Arrange
    final String countryCode = "+93";
    final String expectedTitle = "Enter your phone number";
    final String expectedDescription = "We will text you to verify your number. Standard rates apply.";

    //Act
    TestReporter.addInfoToReport("Launch a basic login");

    AndroidOnboardingScreen androidOnboardingScreen = new AndroidOnboardingScreen();

    AndroidPhoneNumberWidget androidPhoneNumberWidget = androidOnboardingScreen
            .getStarted()
            .setCountryCode(CountryCode.AF, countryCode)
            .setPhoneNumber(PHONE_NUMBER)
            .clickContinueButton()
            .enterValueInEachField(SMS_CODE)
            .acceptAndGoToPermissionsRequestWidget()
            .allowAndContinueToHomeRidesScreen()
            .openSideMenu()
            .logout();

    //Assert
    TestReporter.addInfoToReport("Assert if the title is present in the login screen");
    Assert.assertTrue(androidPhoneNumberWidget.isTitleDisplayed(), "The title is "
            + "not present in the login screen");

    TestReporter.addInfoToReport("Assert if the description is present in the login screen");
    Assert.assertTrue(androidPhoneNumberWidget.isDescriptionDisplayed(), "The description is "
            + "not present in the login screen");

    TestReporter.addInfoToReport("Assert the title on login screen");
    Assert.assertEquals(androidPhoneNumberWidget.getTitleText(), expectedTitle,
            "The title does not match");

    TestReporter.addInfoToReport("Assert the description on My login screen");
    Assert.assertEquals(androidPhoneNumberWidget.getDescriptionText(), expectedDescription,
            "The description does not match");
  }
}