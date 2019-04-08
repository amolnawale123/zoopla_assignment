package com.zoopla.assgmnt.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.zoopla.assgmnt.util.TestUtil;
import com.zoopla.assgmnt.util.WebEventListener;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {

	public static int cntPass = 0;
	public static int cntFail = 0;
	public static int cntSkip = 0;
	public static long executionTime = 0;

	public static Properties prop;
	public SoftAssert softassert = new SoftAssert();

	public static ExtentReports extent;
	public static ExtentTest test;
	public static String reportName;

	public static WebDriver driver;
	public static EventFiringWebDriver e_driver;
	public static WebEventListener eventListener;

	public TestBase() {
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/java/com/zoopla/assgmnt/config/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initialization() {
		String browserName = prop.getProperty("browser");

		if (browserName.equals("chrome")) {
			WindowsUtils.killByName("chromedriver.exe");
			//System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "/src/main/java/com/zoopla/assgmnt/resources/chromedriver.exe");
			WebDriverManager.chromedriver().setup();			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			options.addArguments("disable-gpu");
			options.setPageLoadStrategy(PageLoadStrategy.NONE);
			driver = new ChromeDriver(options);
		} else if (browserName.equals("firefox")) {
			//System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir") + "/src/main/java/com/zoopla/assgmnt/resources/geckodriver.exe");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}

		e_driver = new EventFiringWebDriver(driver);
		// Now create object of EventListerHandler to register it with
		// EventFiringWebDriver
		eventListener = new WebEventListener();
		e_driver.register(eventListener);
		driver = e_driver;

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);

		driver.get(prop.getProperty("url"));

	}

	static {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		reportName = System.getProperty("user.dir") + "/ExtentReports/Zoopla_UI_AutomationReport_" + formater.format(cal.getTime()) + ".html";
		extent = new ExtentReports(reportName, false);
		File file = new File(System.getProperty("user.dir") + "/src/main/java/com/zoopla/assgmnt/config/extent-config.xml");
		extent.loadConfig(file);
		extent.addSystemInfo("Project", "Zoopla UI Automation Testing");
		extent.addSystemInfo("User Name", "Amol Nawale");
		extent.addSystemInfo("User Email Address", "amol.nawale@gmail.com");
	}

	public void getresult(ITestResult result) throws IOException {
		String exceptionAsString = null;
		if (result.getThrowable() != null) {
			StringWriter sw = new StringWriter();
			result.getThrowable().printStackTrace(new PrintWriter(sw));
			exceptionAsString = sw.toString();
		}

		if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(LogStatus.PASS, result.getMethod().getMethodName() + " ==> Test is PASSED");
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP, result.getMethod().getMethodName()
					+ " ==> Test is SKIPPED and skip reason is ======> " + "/n" + exceptionAsString);
		} else if (result.getStatus() == ITestResult.FAILURE) {
			String screenShotPath = capture(driver, result.getMethod().getMethodName());
			InputStream is = new FileInputStream(screenShotPath);
			byte[] imageBytes = IOUtils.toByteArray(is);
			String base64 = Base64.getEncoder().encodeToString(imageBytes);
			test.log(LogStatus.ERROR, result.getMethod().getMethodName()
					+ " ==> Test is FAILED and the reason is ======> " + "/n" + exceptionAsString);
			// test.log(LogStatus.FAIL, "Error Screenshot ==> " +
			// test.addScreenCapture(screenShotPath));
			test.log(LogStatus.FAIL,"Error Screenshot ==> " + test.addBase64ScreenShot("data:image/png;base64," + base64));
		} else if (result.getStatus() == ITestResult.STARTED) {
			test.log(LogStatus.INFO, result.getMethod().getMethodName() + " ==> Test is started");
		}
	}

	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		getresult(result);
		quitDriver();
	}

	@BeforeMethod
	public void beforeMethod(Method result) {
		test = extent.startTest(this.getClass().getSimpleName() + "::" + result.getName(), result.getName());
		test.assignAuthor("Amol Nawale");
		test.log(LogStatus.INFO, result.getName() + " ==> Test Started");
	}

	@AfterClass
	public void endTest() {
		extent.endTest(test);
		extent.flush();
	}

	public void quitDriver() {
		driver.quit();
		driver = null;
	}

	public void sleep(int miliSeconds) {
		try {
			Thread.sleep(miliSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void assertTrue(Boolean blean, String message) {
		softassert.assertTrue(blean, message);
	}

	public void assertFalse(Boolean blean, String message) {
		softassert.assertFalse(blean, message);
	}

	public void assertTrueInt(Boolean blean, String message) {
		softassert.assertTrue(blean, message);
	}

	public void assertEquals(int actual, int expected) {
		softassert.assertEquals(actual, expected);
	}

	public void assertAll() {
		softassert.assertAll();
	}

	public String capture(WebDriver driver, String screenShotName) throws IOException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String dest = System.getProperty("user.dir") + "\\ErrorScreenshots\\" + screenShotName + "-"
				+ formater.format(calendar.getTime()) + ".png";
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);

		return dest;
	}

	// Selenium Generic Wait Methods
	public static void waitAndClick(WebElement locator, int timeout) {
		new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementToBeClickable(locator));
		locator.click();
	}

	public static WebElement waitForElement(WebElement locator, int timeout) {
		new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOf(locator));
		return locator;

	}

	public static List<WebElement> waitForElements(List<WebElement> elements, int timeout) {
		new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOfAllElements(elements));
		return elements;

	}

	public static WebElement waitForElementToBeClickable(WebElement locator, int timeout) {
		new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementToBeClickable(locator));
		return locator;
	}

	public static WebElement waitForElementToBeVisible(WebElement locator, int timeout) {
		new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOf(locator));
		return locator;
	}

	public static WebElement waitForInvisibilityOfElement(WebElement locator, int timeout) {
		new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
				.until(ExpectedConditions.invisibilityOf(locator));
		return locator;
	}

	public static void waitForInvisibilityOfElements(List<WebElement> locator, int timeout) {
		new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
				.until(ExpectedConditions.invisibilityOfAllElements(locator));
	}

	/**
	 * Finds out whether the exception is Element not clickable at point exception,
	 * If it is then does nothing and if its not then throws the exception. Written
	 * to be called inside catch block
	 * 
	 * @param e
	 */
	public void throwsExceptionIfNotElementNotClickable(WebDriverException e) {
		if (e.getMessage().contains("is not clickable at point (")) {

		} else {
			throw e;
		}
	}

}
