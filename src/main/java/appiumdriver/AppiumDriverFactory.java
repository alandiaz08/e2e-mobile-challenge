package appiumdriver;

import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AppiumDriverFactory {

  private AppiumDriver appiumDriver;
  protected static final Logger logger = LogManager.getLogger(appiumdriver.AppiumDriverFactory.class);
  private final String javaVersion = System.getProperty("java.version");
  private final String platform = System.getProperty("platform");
  private final String osVersion = System.getProperty("osVersion");
  private final String device = System.getProperty("device").replace("-", " ");
  private final String appUrl = System.getProperty("appUrl");
  private final String driverMode = System.getProperty("driverMode");
  private final String environment = System.getProperty("environment");
  private AppiumMode appiumMode;
  private MobilePlatform mobilePlatform;
  private URL appiumServerUrl;
  private Local local;
  private static final int WAIT = 1000;
  private final DesiredCapabilities caps;

  public AppiumDriverFactory() {
    caps = new DesiredCapabilities();
    appiumServerUrl = null;
  }

  public void resetDriver() {
    appiumDriver = null;
  }

  public AppiumDriver getDriver() {
    if (null == appiumDriver) {
      logger.trace("Starting AppiumDriver");
      instantiateAppiumDriver();
    }
    return appiumDriver;
  }

  public void quitDriver() {
    logger.trace("Closing AppiumDriver");
    if (null != appiumDriver) {
      appiumDriver.quit();
      appiumDriver = null;
      logger.debug("AppiumDriver is quit");
    }
  }

  private void instantiateAppiumDriver() {
    logger.info("Java Version: {}", javaVersion);
    logger.info("Platform: {}", platform);
    logger.info("OS Version: {}", osVersion);
    logger.info("Device: {}", device);
    logger.info("App Url: {}", appUrl);
    logger.info("Driver Mode: {}", driverMode);

    setCapabilities();
    setAppiumMode();
    setAppiumServerUrl();
    startAppiumDriver();
  }

  private void setAppiumMode() {
    appiumMode = AppiumMode.getEnum(driverMode.toLowerCase());
    logger.info("Selected mode: {}", appiumMode);
  }

  private void setMobilePlatform() {
    logger.info("Default platform: android");
    mobilePlatform = MobilePlatform.ANDROID;
  }

  private void setCapabilities() {
    logger.debug("Set capabilities");
    String build = "Local Test";
    caps.setCapability("build", build);
    logger.debug("Capabilities [build]: {}", build);
    String name = "XXXX";
    caps.setCapability("name", name);
    logger.debug("Capabilities [name]: {}", name);

    caps.setCapability("deviceName", device);
    logger.debug("Capabilities [device]: {}", device);
    caps.setCapability("os_version", osVersion);
    logger.debug("Capabilities [os_version]: {}", osVersion);
    caps.setCapability("app", appUrl);
    logger.debug("Capabilities [app]: {}", appUrl);
    caps.setCapability("browserstack.debug", "true");
    logger.debug("Capabilities [browserstack.debug]: {}", "true");
    caps.setCapability("browserstack.networkLogs", "true");
    logger.debug("Capabilities [networkLogs]: {}", "true");
    caps.setCapability("disableAnimations", "true");
    logger.debug("Capabilities [disableAnimations]: {}", "true");
    setMobilePlatform();
    String project = "App";
    caps.setCapability("project", project);
    logger.debug("Capabilities [project]: {}", project);
    caps.setCapability("automationName", "UiAutomator2");
    logger.debug("Capabilities [automationName]: {}", "UiAutomator2");
    caps.setCapability("appPackage", "com.hdw.james.rider");
    logger.debug("Capabilities [appPackage]: com.hdw.james.rider");
    caps.setCapability("appActivity", "com.hdw.james.rider.viewlayer.launcher.LauncherActivity");
    logger.debug("Capabilities [appActivity]: com.hdw.james.rider.viewlayer.launcher.LauncherActivity");
    caps.setCapability("language", "en");
    logger.debug("Capabilities [language]: {}", "en");
    caps.setCapability("locale", "FR");
    logger.debug("Capabilities [locale]: {}", "FR");
    caps.setCapability("skipDeviceInitialization", true);
    logger.debug("Skip device initialization {}", "true");
    caps.setCapability("adbExecTimeout", 50000);
    logger.debug("Set Adb exec timeout in millisecs {}", "50000");
  }

  /**
   * Starts the Appium driver with the specified capabilities for Android.
   */
  private void startAppiumDriver() {
    logger.debug("Starting Appium driver for Android");
    setMobilePlatform();
    if (mobilePlatform == MobilePlatform.ANDROID) {
      setAndroidLocalApp();
      appiumDriver = new AndroidDriver<>(appiumServerUrl, caps);
      logger.debug("Started Android Appium driver");
    } else {
      throw new IllegalArgumentException("The platform [" + mobilePlatform + "] is unknown. Use android.");
    }
  }

  private void setAndroidLocalApp() {
    String localAndroidAppName = "James_Rider_1.22.0.apk";
    logger.info("Set local Android app: {}", localAndroidAppName);
    File app = new File(localAndroidAppName);
    caps.setCapability("app", app.getAbsolutePath());
  }

  public MobilePlatform getMobilePlatform() {
    logger.trace("Getting mobile platform");
    return mobilePlatform;
  }

  public String getDevice() {
    logger.trace("Getting mobile device");
    return device;
  }

  public String getEnvironment() {
    logger.trace("Getting execution environment");
    return environment;
  }

  private void setAppiumServerUrl() {
    logger.debug("Set appium server URL");
    if (appiumMode == AppiumMode.LOCAL) {
      logger.debug("Local Appium Server URL");
      appiumServerUrl = getAppiumUrlLocal();
    } else {
      logger.info("Appium mode not known, default to browserstack");
    }
  }

  private static URL getAppiumUrlLocal() {
    URL localAppiumServerUrl = null;
    try {
      localAppiumServerUrl = new URL("http://127.0.0.1:4723/wd/hub");
    } catch (MalformedURLException e) {
      logger.error(e);
    }
    return localAppiumServerUrl;
  }
}
