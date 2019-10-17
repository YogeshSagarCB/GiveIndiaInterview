package com.giveindia.interviewtest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.giveindia.interviewtest.utility.ExtentUtility;
import com.relevantcodes.extentreports.LogStatus;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Stepdefs {

	WebDriver driver;
	ExtentUtility extentUtility;

	@Given("^I am on Selenium wiki page$")
	public void i_am_on_Selenium_wiki_page() throws Exception {
		extentUtility = new ExtentUtility();
		WebDriverManager.chromedriver().setup();
		extentUtility.startTest();
		extentUtility.startTest("Wiki Test");
		extentUtility.addStep(LogStatus.INFO, "Opened browser");
		driver = new ChromeDriver();
		extentUtility.addStep(LogStatus.INFO, "Maximizing browser");
		driver.manage().window().maximize();
		extentUtility.addStep(LogStatus.INFO, "navigating to selenium wiki page");
		String url = "https://en.wikipedia.org/wiki/Selenium";
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String actualUrl = driver.getCurrentUrl();
		if (actualUrl.equals(url)) {
			extentUtility.addStep(LogStatus.PASS, "navigated to selenium wiki page");
		} else {
			extentUtility.addStep(LogStatus.FAIL, "error in navigating to selenium wiki page");
		}
	}

	@Then("^I verify all external links work$")
	public void i_verify_all_external_links_work() throws Exception {
		extentUtility.addStep(LogStatus.INFO, "verifyinh external links");
		List<WebElement> externalLinks = driver
				.findElements(By.xpath("//span[@id='External_links']/../following-sibling::ul/li/a"));
		ChromeOptions chromeOptions = new ChromeOptions();
		ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
		chromeDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		for (WebElement externalLink : externalLinks) {
			chromeDriver.get(externalLink.getAttribute("href"));
			extentUtility.addStep(LogStatus.PASS, "external link works : " + chromeDriver.getCurrentUrl());
		}
		chromeDriver.close();
		chromeDriver.quit();
	}

	@When("^I click on Oxygen in periodic table$")
	public void i_click_on_Oxygen_in_periodic_table() throws Exception {
		driver.findElement(By.xpath("(//a[contains(@href,'Oxygen')])[2]")).click();
	}

	@Then("^I verify navigation to Oxygen wiki page\\.$")
	public void i_verify_navigation_to_Oxygen_wiki_page() throws Exception {
		String url = driver.getCurrentUrl();
		if (url.equals("https://en.wikipedia.org/wiki/Oxygen")) {
			extentUtility.addStep(LogStatus.PASS, "Navigated to oxygen wiki page");
		}
	}

	@Then("^I verify it is a feature article$")
	public void i_verify_it_is_a_feature_article() throws Exception {
		WebElement featured = driver.findElement(By.xpath("//img[contains(@alt,'This is a featured article')]"));
		if (featured.isDisplayed()) {
			extentUtility.addStep(LogStatus.PASS, "It is featured article");
		}
	}

	@Then("^I take screenshot of properties of Oxygen$")
	public void i_take_screenshot_of_properties_of_Oxygen() throws Exception {
		extentUtility.addStep(LogStatus.PASS, "Taking screenshot of properties");
		WebElement properties = driver.findElement(By.xpath("//table[@class='infobox']"));
		Screenshot screenshot=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver); 
		String filePath = System.getProperty("user.dir")+"/target/properties.png";
		ImageIO.write(screenshot.getImage(),"PNG",new File(filePath));
		BufferedImage fullImg = ImageIO.read(new File(filePath));
		Point point = properties.getLocation();
		int eleWidth = properties.getSize().getWidth();
		int eleHeight = properties.getSize().getHeight();
		BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", new File(System.getProperty("user.dir")+"/target/properties_cropped.png"));
	}

	@Then("^I count number of pdf links in references$")
	public void i_count_number_of_pdf_links_in_references() throws Exception {
		List<WebElement> references = driver
				.findElements(By.xpath("//span[@id='References']/../following-sibling::div/ol/li//a"));
		int count = 0;
		for (WebElement reference : references) {
			if (reference.getAttribute("href").endsWith(".pdf")) {
				count++;
			}
		}
		extentUtility.addStep(LogStatus.PASS, "Number of pdf references : " + count);
	}

	@When("^I search for pluto$")
	public void i_search_for_pluto() throws Exception {
		driver.findElement(By.id("searchInput")).sendKeys("pluto");
		extentUtility.addStep(LogStatus.PASS, "Searching for pluto");
	}

	@Then("^I verify second suggestion is plutonium$")
	public void i_verify_second_suggestion_is_plutonium() throws Exception {
		try {
			WebElement secondResult = driver.findElement(By.xpath("//div[@class='suggestions-results']/a[2]"));
			String result = secondResult.getAttribute("title");
			if(result.equals("Plutonium")) {
				extentUtility.addStep(LogStatus.PASS, "Second result is Plutonium");
			} else {
				extentUtility.addStep(LogStatus.FAIL, "Second result is not Plutonium");
			}
			driver.close();
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		extentUtility.endTest();
		extentUtility.endTestReport();
	}
}