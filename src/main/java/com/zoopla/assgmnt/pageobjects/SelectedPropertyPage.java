package com.zoopla.assgmnt.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;
import com.zoopla.assgmnt.base.TestBase;

public class SelectedPropertyPage extends TestBase {
	private static final Logger logger = Logger.getLogger(SelectedPropertyPage.class);

	public static String agentFullName = null;
	public static String agentContactNumber = null;
	public static String agentAddress = null;

	@FindBy(xpath = "//h1[contains(@class,'ui-property-summary')]")
	public WebElement propertyName;

	@FindBy(xpath = "//div[contains(@class,'sidebar-wrapper')]//p[contains(@class,'ui-pricing')]")
	public WebElement propertyValue;

	@FindBy(xpath = "//h2[contains(@class,'address')]")
	public WebElement propertyAddress;

	@FindBy(xpath = "//div[contains(@class,'sidebar-wrapper')]//img")
	public WebElement agentLogo;

	@FindBy(xpath = "//div[contains(@class,'sidebar-wrapper')]//h4[@class='ui-agent__name']")
	public WebElement agentName;

	@FindBy(xpath = "//div[contains(@class,'sidebar-wrapper')]//a[@class='ui-link']")
	public WebElement agentPhoneNumber;

	// Initializing the Page Objects:
	public SelectedPropertyPage() {
		PageFactory.initElements(driver, this);
	}

	/**
	 * This method verifies the agent details.
	 */
	public void printAgentDetails() {
		logger.debug("Printing the agent details");
		test.log(LogStatus.INFO, "Printing the agent details");
		sleep(4000);
		agentFullName = waitForElement(agentName, 10).getText().trim();
		agentContactNumber = agentPhoneNumber.getText().trim();
		String str = propertyAddress.getText().trim();
		System.out.println(str);
		String[] abc= str.split("\\s");
		agentAddress = abc[abc.length - 1];
		

		System.out.println("On the Selected Propert Page, Agent Name is : " + agentFullName);
		System.out.println("On the Selected Propert Page, Agent Phone Number is : " + agentContactNumber);
		System.out.println("On the Selected Propert Page, Agent Address Code is : "+agentAddress);
		System.out.println();

		Boolean ImagePresent = (Boolean) ((JavascriptExecutor) driver).executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
				agentLogo);

		assertTrue(ImagePresent, "Agent Logo is missing");
		assertTrue(agentName.isDisplayed(), "Agent Name is missing");
		assertTrue(agentPhoneNumber.isDisplayed(), "Agent Phone Number is missing");
		//assertAll();
	}

	/**
	 * This method clicks on the Agent Name
	 */
	public AgentPage clickAgentName() {
		logger.debug("Click on the Agent Name");
		test.log(LogStatus.INFO, "Click on the Agent Name");
		agentName.click();
		return new AgentPage();
	}

}
