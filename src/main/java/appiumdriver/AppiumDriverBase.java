package appiumdriver;

import environment.EnvironmentConfig;
import io.appium.java_client.AppiumDriver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class AppiumDriverBase {

  /**
   * Logger.
   */
  protected static final Logger logger = LogManager.getLogger(appiumdriver.AppiumDriverBase.class);

  private static final String THREAD_ID = "threadId";

  /**
   * List of safe AppiumDriverThreads to keep each AppiumDriver in a different thread.
   */
  private static final List<AppiumDriverFactory> appiumDriverThreadPool = Collections
          .synchronizedList(new ArrayList<>());

  /**
   * ThreadLocal variable to store individually each driver in each thread.
   */
  private static ThreadLocal<AppiumDriverFactory> appiumDriverThread;

  /**
   * Protected constructor.
   */
  protected AppiumDriverBase() {
  }

  /**
   * Instantiate the AppiumDriverFactory object at the beginning of the test suite, and store it in
   * a ThreadLocal variable to keep each AppiumDriverFactory and each AppiumDriver isolated in each
   * thread.
   */
  @BeforeSuite(alwaysRun = true)
  public static void startSuite() {

    ThreadContext.put(THREAD_ID, Thread.currentThread().getName());
    EnvironmentConfig.initializeEnvironment();

    // Instantiates and stores the AppiumDriver into the ThreadLocal variable
    appiumDriverThread = ThreadLocal.withInitial(() -> {
      /*
       The following instruction is used to tell log4j which file the log will be written to
       depending on the thread name. The thread name is used as the value threadId that is used as
       the routing key in the routing appender. Check the log4j2.xml config file.
       For more info check here:
       https://stackoverflow.com/questions/8355847/how-to-log-multiple-threads-in-different-log-files
       http://logging.apache.org/log4j/2.x/faq.html#separate_log_files
      */
      ThreadContext.put(THREAD_ID, Thread.currentThread().getName());

      logger.trace("Instantiate AppiumDriver");
      AppiumDriverFactory appiumDriverFactory = new AppiumDriverFactory();
      appiumDriverThreadPool.add(appiumDriverFactory);
      return appiumDriverFactory;
    });
  }

  /**
   * Gets the AppiumDriver from the AppiumDriverFactory using a singleton pattern.
   *
   * @return the instantiated AppiumDriver
   */
  public static AppiumDriver getDriver() {
    logger.trace("Get the AppiumDriver");

    // Gets the AppiumDriver stored in the ThreadLocal variable
    return appiumDriverThread.get().getDriver();
  }

  /**
   * Start Appium.
   */
  @BeforeMethod(alwaysRun = true)
  public static void initiateAppiumDriver() {
    try {
      logger.trace("Start appium");
      appiumDriverThread.get().resetDriver();
    } catch (Exception ex) {
      logger.error("Unable to start appium", ex);
    }
  }

  /**
   * Quit the browser between tests.
   */
  @AfterMethod(alwaysRun = true, dependsOnMethods = { "finishTest" })
  public static void quitAppiumDriver() {
    try {
      logger.debug("Get the session id of the test before quitting");
      logger.trace("Quit AppiumDriver");
      appiumDriverThread.get().quitDriver();
    } catch (Exception ex) {
      logger.error("Unable to quit AppiumDriver", ex);
    }
  }

  /**
   * Safely quits all the AppiumDrivers in the ThreadPool.
   */
  @AfterSuite(alwaysRun = true)
  public static void finishSuite() {
    /*
     The following instruction is used to tell log4j which file the log will be written to depending
     on the thread name. The thread name is used as the value threadId that is used as the routing
     key in the routing appender. Check the log4j2.xml config file.
     For more info check here:
     https://stackoverflow.com/questions/8355847/how-to-log-multiple-threads-in-different-log-files
     http://logging.apache.org/log4j/2.x/faq.html#separate_log_files
    */
    ThreadContext.put(THREAD_ID, Thread.currentThread().getName());

    int index = 0;
    for (AppiumDriverFactory appiumDriverFactory : appiumDriverThreadPool) {
      logger.trace("Quit AppiumDriver {}", index);
      index++;
      appiumDriverFactory.quitDriver();
    }
    logger.trace("Remove AppiumDriver from ThreadLocal");
    appiumDriverThread.remove();
  }

  /**
   * Gets the MobilePlatform.
   *
   * @return MobilePlatform
   */
  public static MobilePlatform getMobilePlatform() {
    logger.trace("Get the BrowserType");
    return appiumDriverThread.get().getMobilePlatform();
  }

  /**
   * Gets the device.
   *
   * @return device
   */
  public static String getMobileDevice() {
    logger.trace("Get the Device");
    return appiumDriverThread.get().getDevice();
  }

  /**
   * Gets the environment.
   *
   * @return environment prod/preprod.
   */
  public static String getEnvironment() {
    logger.trace("Get the environment");
    return appiumDriverThread.get().getEnvironment();
  }
}
