package com.zoopla.assgmnt.testcases;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zoopla.assgmnt.base.TestBase;
import com.zoopla.assgmnt.pageobjects.HomePage;
import com.zoopla.assgmnt.pageobjects.SearchResultsPage;
import com.zoopla.assgmnt.util.TestUtil;

public class SelectFifthPropertyTest extends TestBase {
	HomePage homePage;
	SearchResultsPage searchResultsPage;
	TestUtil testUtil;
	
	public SelectFifthPropertyTest() {
		super();
	}
	
	@BeforeMethod
	public void setUp() {
		initialization();
		testUtil = new TestUtil();
		homePage = new HomePage();	
		searchResultsPage = new SearchResultsPage();
	}
	
	@Test
	public void selectFifthPropertyTest() {
		test.assignCategory("Select Fifth Property");
		searchResultsPage = homePage.enterLocation();
		searchResultsPage.clickFifthPropertyForSale();
		
	}


}
