package com.zoopla.assgmnt.testcases;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zoopla.assgmnt.base.TestBase;
import com.zoopla.assgmnt.pageobjects.AgentPage;
import com.zoopla.assgmnt.pageobjects.HomePage;
import com.zoopla.assgmnt.pageobjects.SearchResultsPage;
import com.zoopla.assgmnt.pageobjects.SelectedPropertyPage;
import com.zoopla.assgmnt.util.TestUtil;

public class ValidateAgentNameDetailsTest extends TestBase {
	HomePage homePage;
	SearchResultsPage searchResultsPage;
	SelectedPropertyPage selectedPropertyPage;
	AgentPage agentPage;
	TestUtil testUtil;
	String agentFullName,agentAddress;
	
	public ValidateAgentNameDetailsTest() {
		super();
	}
	
	@BeforeMethod
	public void setUp() {
		initialization();
		testUtil = new TestUtil();
		homePage = new HomePage();	
		searchResultsPage = new SearchResultsPage();
		selectedPropertyPage= new SelectedPropertyPage();
		agentPage=new AgentPage();
	}
	
	@Test
	public void validateAgentNameDetailsTest() {
		test.assignCategory("Validate Agent Details");
		searchResultsPage = homePage.enterLocation();
		selectedPropertyPage = searchResultsPage.clickFifthPropertyForSale();
		selectedPropertyPage.printAgentDetails();
		agentPage=selectedPropertyPage.clickAgentName();
		agentPage.verifyAgentDetails(agentFullName, agentAddress);		
	}


}
