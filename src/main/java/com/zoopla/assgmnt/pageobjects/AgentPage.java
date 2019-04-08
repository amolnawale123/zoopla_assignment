package com.zoopla.assgmnt.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;
import com.zoopla.assgmnt.base.TestBase;

public class AgentPage extends TestBase {
	private static final Logger logger = Logger.getLogger(AgentPage.class);

	@FindBy(xpath = "//h1/b[1]")
	public WebElement agentName;

	@FindBy(xpath = "//h1/b[2]")
	public WebElement weAgentAddress;

	// Initializing the Page Objects:
	public AgentPage() {
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * This method verifies the Agent details such as name and address.
	 */
	public void verifyAgentDetails(String agentFullName, String agentAddress) {
		logger.debug("Verifying the agent details");
		test.log(LogStatus.INFO, "Verifying the agent details");
		System.out.println("On the Agent Page, Agent Name is : "+agentName.getText().trim());
		System.out.println("On the Agent Page, Agent Address is : "+weAgentAddress.getText().trim());
		assertTrue(agentName.getText().trim().equals(agentFullName), "Agent Name is not matching");
		assertTrue(weAgentAddress.getText().trim().equals(agentFullName), "Agent Address is not matching");		
	}

}
