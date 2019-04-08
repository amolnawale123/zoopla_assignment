package com.zoopla.assgmnt.testcases;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zoopla.assgmnt.base.TestBase;
import com.zoopla.assgmnt.pageobjects.HomePage;
import com.zoopla.assgmnt.pageobjects.SearchResultsPage;
import com.zoopla.assgmnt.pageobjects.SelectedPropertyPage;
import com.zoopla.assgmnt.util.TestUtil;

public class ClickAgentNameOnSelectedFifthPropertyTest extends TestBase {
	HomePage homePage;
	SearchResultsPage searchResultsPage;
	SelectedPropertyPage selectedPropertyPage;
	TestUtil testUtil;
	
	public ClickAgentNameOnSelectedFifthPropertyTest() {
		super();
	}
	
	@BeforeMethod
	public void setUp() {
		initialization();
		testUtil = new TestUtil();
		homePage = new HomePage();	
		searchResultsPage = new SearchResultsPage();
		selectedPropertyPage= new SelectedPropertyPage();
	}
	
	@Test
	public void clickAgentNameTest() {
		test.assignCategory("Click Agent Name");
		searchResultsPage = homePage.enterLocation();
		selectedPropertyPage = searchResultsPage.clickFifthPropertyForSale();
		selectedPropertyPage.printAgentDetails();
		selectedPropertyPage.clickAgentName();		
	}


}
