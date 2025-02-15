package utils;

import appiumdriver.AppiumDriverBase;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Utility class to log info and screenshots in the test report.
 */
public final class TestReporter {

  /**
   * The logger.
   */
  private static final Logger logger = LogManager.getLogger(TestReporter.class);

  private static final RandomStringGenerator filenameGenerator =
          new RandomStringGenerator(15, new SecureRandom(),
                  RandomStringGenerator.LOWERCASE);

  private static ExtentSparkReporter sparkReporter;
  private static ExtentTest test;
  private static ExtentReports extentReport;

  /**
   * Keeps the number of the last message displayed on the test report log of each test. Useful
   * for tracking metrics on where in the scenario the test failed.
   */
  private static ThreadLocal<Integer> threadStepNumber = ThreadLocal.withInitial(() -> 1);

  /**
   * Private constructor to hide the implicit one.
   */
  private TestReporter() {
  }

  /**
   * Initializes the ExtentReports.
   *
   * @param docTitle the title of the document
   */
  public static void initializeReporter(String docTitle) {
    if (extentReport != null) {
      return;
    }

    extentReport = new ExtentReports();
    String projectPath = System.getProperty("user.dir");
    sparkReporter = new ExtentSparkReporter(projectPath + "/build/extent/HtmlReport/extent.html");

    extentReport.attachReporter(sparkReporter);

    sparkReporter.config().setOfflineMode(true);
    sparkReporter.config().setDocumentTitle(docTitle);
    sparkReporter.config().setReportName("Test Report");
  }

  /**
   * Creates a new test in the ExtentReports.
   *
   * @param testName the name of the test
   */
  public static void createTest(String testName) {
    test = extentReport.createTest(testName);
  }

  /**
   * Resets the step counter to 1.
   */
  public static void resetStepCounter() {
    logger.debug("The step counter was reset to 1");
    threadStepNumber.set(1);
  }

  /**
   * Gets the number of the current step to be logged.
   */
  public static int getCurrentStepNumber() {
    return threadStepNumber.get();
  }

  /**
   * Logs info to the Extent test report and to Log4j2.
   *
   * @param message the message to log
   */
  public static void addInfoToReport(String message) {
    logger.debug(message);
    int currentStep = threadStepNumber.get();
    String messageWithStepNumber = Integer.toString(currentStep) + " - " + message;
    currentStep++;
    threadStepNumber.set(currentStep);
    test.log(Status.INFO, messageWithStepNumber);
  }

  /**
   * Takes a screenshot and adds it to ExtentReport.
   *
   * @param message the message to log with the screenshot
   */
  public static void addScreenshotToReport(String message) {
    logger.debug(message);

    logger.debug("Taking screenshot");
    File scrFile = ((TakesScreenshot) AppiumDriverBase.getDriver()).getScreenshotAs(OutputType.FILE);
    String projectPath = System.getProperty("user.dir");
    logger.debug("Project path: {}", projectPath);
    String extentReportPath = projectPath + "/build/extent/HtmlReport/";
    String fileName = filenameGenerator.nextString() + ".png";
    String fullFilenamePath = extentReportPath + fileName;
    logger.debug("Full path screenshot file name: {}", fullFilenamePath);

    try {
      FileUtils.copyFile(scrFile, new File(fullFilenamePath));
    } catch (IOException e) {
      logger.error("Exception creating the screenshot file", e);
      return;
    }

    int currentStep = threadStepNumber.get();
    String messageWithStepNumber = Integer.toString(currentStep) + " - " + message;
    currentStep++;
    threadStepNumber.set(currentStep);
    test.log(Status.INFO, messageWithStepNumber + " ------ Screenshot: ",
            MediaEntityBuilder.createScreenCaptureFromPath(fileName, fileName).build());
  }

  /**
   * Logs an error message to the Extent test report and to Log4j2.
   *
   * @param text the error message
   */
  public static void reportError(String text) {
    addMessage(Status.FAIL, text);
  }

  /**
   * Logs a success message to the Extent test report and to Log4j2.
   *
   * @param text the success message
   */
  public static void reportSuccess(String text) {
    addMessage(Status.PASS, text);
  }

  /**
   * Logs an info message to the Extent test report and to Log4j2.
   *
   * @param text the info message
   */
  public static void reportInfo(String text) {
    addMessage(Status.INFO, text);
  }

  private static void addMessage(Status messageType, String text) {
    test.log(messageType, text);
  }

  /**
   * Flushes the ExtentReports, ensuring that all logs are written to the report.
   */
  public static void flushReport() {
    extentReport.flush();
  }
}

