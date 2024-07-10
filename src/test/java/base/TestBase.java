package base;

import appiumdriver.AppiumDriverBase;
import com.aventstack.extentreports.testng.listener.ExtentITestListenerAdapter;
import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.jetbrains.annotations.NotNull;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.TestReporter;

@Listeners({ExtentITestListenerAdapter.class})
public class TestBase extends AppiumDriverBase {


  /**
   * The logger.
   */
  protected static final Logger logger = LogManager.getLogger(TestBase.class);

  private static final String SEPARATOR = " -------- ";

  protected int retryCounter;

  /**
   * Logs the start of each test.
   *
   * @param method The method (test) being executed
   */
  @BeforeMethod(alwaysRun = true)
  public void startTest(final @NotNull Method method) {
    /*
     The following instruction is used to tell log4j which file the log will be written to depending
     on the thread name. The thread name is used as the value threadId that is used as the routing
     key in the routing appender. Check the log4j2.xml config file.
     For more info check here:
     https://stackoverflow.com/questions/8355847/how-to-log-multiple-threads-in-different-log-files
     http://logging.apache.org/log4j/2.x/faq.html#separate_log_files
    */
    ThreadContext.put("threadId", Thread.currentThread().getName());

    // resets the test report step counter to 1
    TestReporter.resetStepCounter();

    logger.info(SEPARATOR + "Starting test " + method.getName() + SEPARATOR);
  }

  /**
   * Logs the end of each test and the test result in ExtentReport.
   *
   * @param result the test result after the execution
   */
  @AfterMethod(alwaysRun = true)
  public synchronized void finishTest(@NotNull ITestResult result) {

    // Write a Test Report log to identify which thread the test ran on
    TestReporter.addInfoToReport("The test logs have been saved in: "
            + Thread.currentThread().getName() + ".log");

    // Write logs and take screenshot if failed or skipped
    if (result.getStatus() == ITestResult.FAILURE) {
      TestReporter.addScreenshotToReport("Screenshot of failed test");
      logger.info("Test failed");
      logger.error(result.getThrowable());
    } else if (result.getStatus() == ITestResult.SKIP) {
      TestReporter.addScreenshotToReport("Screenshot of skipped test");
      logger.info("Test failure skipped, retry test");
      logger.debug(result.getThrowable());
    } else if (result.getStatus() == ITestResult.SUCCESS) {
      logger.info("Test passed");
    } else {
      logger.info("Test result: {}", result.getStatus());
    }

    String appVersion = System.getProperty("appVersion");
    String env = System.getProperty("environment");
    String device = System.getProperty("device");
    String branch = System.getProperty("branchName");
    TestReporter.addInfoToReport(SEPARATOR + "This test was executed on the " + env
            + " App Version: " + appVersion + " on " + device + SEPARATOR);
    TestReporter.addInfoToReport(SEPARATOR + "Branch name is " + branch + SEPARATOR);

    logger.info(SEPARATOR + "Finished test " + result.getName() + SEPARATOR);
  }


  public static class RetryAnalyzer implements IRetryAnalyzer {

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(TestBase.RetryAnalyzer.class);

    private int counter;
    private static final int RETRY_LIMIT = 2;

    public RetryAnalyzer() {
      logger.debug("Retry analyzer constructor");
    }

    public int getRetryCount() {
      return counter;
    }

    @Override
    public boolean retry(ITestResult result) {
      if (counter < RETRY_LIMIT) {
        logger.info("Going to retry test case: " + result.getMethod().getMethodName()
                + ", " + (counter + 1) + " out of " + RETRY_LIMIT);
        counter++;
        return true;
      }
      return false;
    }
  }
}

