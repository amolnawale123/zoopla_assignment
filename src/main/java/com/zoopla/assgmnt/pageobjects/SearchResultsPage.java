package com.zoopla.assgmnt.pageobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;
import com.zoopla.assgmnt.base.TestBase;

public class SearchResultsPage extends TestBase {
	private static final Logger logger = Logger.getLogger(SearchResultsPage.class);

	JavascriptExecutor js = ((JavascriptExecutor) driver);
	
	public List<String> priceList;
	public List<String> modifiedPriceList;
	public static String selectedPropertyName = null;

	@FindBy(xpath = "//button/i[contains(@class,'icon-cancel')]")
	public WebElement filterTooltipCancelBtn;

	@FindBy(xpath = "//ul[contains(@class,'listing-results clearfix')]/li//a[@class='listing-results-price text-price']")
	public List<WebElement> propertyPriceList;

	// Initializing the Page Objects:
	public SearchResultsPage() {
		PageFactory.initElements(driver, this);
	}

	/**
	 * This methods sorts the property value in descending order
	 */
	public void sortPropertyValuesDescending() {
		logger.debug("Sorting the Property Values in Descending order");
		test.log(LogStatus.INFO, "Sorting the Property Values in Descending order");
		System.out.println("Sorting the Property Values in Descending order");
		sleep(5000);
		try {
			System.out.println("In the Try block");
			filterTooltipCancelBtn.click();
		} catch (Exception e) {
			System.out.println("Filter Tool Tip is not present");
		}
		
		priceList = new ArrayList<String>();
		for (int i = 0; i < propertyPriceList.size(); i++) {
			String value = propertyPriceList.get(i).getText().trim();
			priceList.add(value);
		}

		// Removing POA from the list
		priceList.remove("POA");

		modifiedPriceList = new ArrayList<>();

		// Removing the texts such as Guide Price from certain Property values
		for (String str : priceList) {
			String arr[] = str.split("\\s");

			modifiedPriceList.add(arr[0]);
		}

		System.out.println("Modified Price List before sorting with the POA and Guide Price texts removed");
		for (String str : modifiedPriceList) {
			System.out.println(str);
		}

		System.out.println();

		List<Long> longList = new ArrayList<>();

		for (String abc : modifiedPriceList) {
			abc = abc.replaceAll(",", "");
			abc = abc.replaceAll("£", "");
			longList.add(Long.parseLong(abc));
		}

		System.out.println();

		// Sorting the new Long list in descending order
		Collections.sort(longList, Collections.reverseOrder());

		System.out.println();
		System.out.println("The List after formating with commas is as below");
		List<String> newList = new ArrayList<>();
		String regex = "(\\d)(?=(\\d{3})+$)";
		for(Long def:longList) {
			newList.add("£"+String.valueOf(def).replaceAll(regex, "$1,"));
		}

		for(String abc:newList) {
			System.out.println(abc);
		}

	}

	/**
	 * This method clicks on the fifth property from the searched property list
	 * 
	 * @return SelectedPropertyPage
	 */
	public SelectedPropertyPage clickFifthPropertyForSale() {
		logger.debug("Selecting the Fifth Property");
		test.log(LogStatus.INFO, "Selecting the Fifth Property");
		sleep(5000);
		for (int i = 0; i < propertyPriceList.size(); i++) {
			if (i == 4) {
				selectedPropertyName = propertyPriceList.get(i).getText().trim();
				System.out.println("Selected fifth Property to click is : " + selectedPropertyName);
				js.executeScript("arguments[0].scrollIntoView(true);", propertyPriceList.get(i));
				js.executeScript("arguments[0].click();", propertyPriceList.get(i));
				break;
			} else {
				System.out.println("We are yet to reach the fifth property");
				continue;
			}
		}

		return new SelectedPropertyPage();
	}

}
