package com.zoopla.assgmnt.testcases;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zoopla.assgmnt.base.TestBase;
import com.zoopla.assgmnt.pageobjects.HomePage;
import com.zoopla.assgmnt.pageobjects.SearchResultsPage;
import com.zoopla.assgmnt.util.TestUtil;

public class PrintPropertyValueDescendingTest extends TestBase {
	HomePage homePage;
	SearchResultsPage searchResultsPage;
	TestUtil testUtil;
	
	public PrintPropertyValueDescendingTest() {
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
	public void searchAndPrintPropertyValuesDescendingTest() {
		test.assignCategory("Print Property Value");
		searchResultsPage = homePage.enterLocation();
		searchResultsPage.sortPropertyValuesDescending();
		
	}


}
