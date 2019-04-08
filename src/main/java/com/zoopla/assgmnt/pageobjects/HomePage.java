package com.zoopla.assgmnt.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;
import com.zoopla.assgmnt.base.TestBase;

public class HomePage extends TestBase {
	private static final Logger logger = Logger.getLogger(HomePage.class);

	@FindBy(xpath = "//input[@id='search-input-location']")
	public WebElement searchTxtBox;

	@FindBy(xpath = "//button[@id='search-submit']")
	public WebElement searchBtn;

	// Initializing the Page Objects:
	public HomePage() {
		PageFactory.initElements(driver, this);
	}
	
	public SearchResultsPage enterLocation() {
		logger.debug("Entering the location to be searched");
		test.log(LogStatus.INFO, "Entering the location to be searched");
		System.out.println("Entering the location to be searched");
		waitForElement(searchTxtBox, 10);
		searchTxtBox.sendKeys(prop.getProperty("location"));
		searchBtn.click();
		return new SearchResultsPage();
	}

}
