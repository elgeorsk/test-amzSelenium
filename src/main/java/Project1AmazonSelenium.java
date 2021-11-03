import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Project1AmazonSelenium {
	
	public static void main (String args[]) throws InterruptedException, ClassNotFoundException, IOException {

		Logger log = LoggerFactory.getLogger(Project1AmazonSelenium.class);

		//init DB
		if (DBUtil.getConnection() != null){
			log.info("DB connection success!");
			// delete table
			DBUtil.deleteTable(DBUtil.getConnection());
			// create table
			DBUtil.createTable(DBUtil.getConnection());
		} else {
			log.error("DB connection failed!");
		}

		System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		//options.addArguments("start-maximized","--start-fullscreen");
		options.addArguments("--headless","--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(options);
		Thread.sleep(2000);

		TakesScreenshot ts = (TakesScreenshot) driver;
		File scr;

		driver.get("https://www.amazon.com");

		// amz home page screenshot
		scr = ts.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File("src/main/resources/screenshots/amzHomePage-"+ new Timestamp(System.currentTimeMillis()).getTime() +".png"));

		Select amzSearchDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"searchDropdownBox\"]")));
		WebElement amzSearchBox = driver.findElement(By.xpath("//*[@id=\"twotabsearchtextbox\"]"));
		WebElement amzSearchBtn = driver.findElement(By.xpath("//*[@id=\"nav-search-submit-button\"]"));

		amzSearchDropdown.selectByValue("search-alias=stripbooks-intl-ship");
		amzSearchBox.sendKeys("Java Automation");

		// amz home page search screenshot
		scr = ts.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File("src/main/resources/screenshots/amzHomePageSearch-"+ new Timestamp(System.currentTimeMillis()).getTime() +".png"));

		amzSearchBtn.click();

		List<WebElement> amzSearchList = driver.findElements(By.className("s-matching-dir")).get(0)
						.findElements(By.className("s-include-content-margin"));

		Iterator<WebElement> iteratorBooks = amzSearchList.iterator();
		List<AmzBookItem> books = new ArrayList<>();

		// amz search page results screenshot
		scr = ts.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File("src/main/resources/screenshots/amzSearchPageResults-"+ new Timestamp(System.currentTimeMillis()).getTime() +".png"));

		while(iteratorBooks.hasNext()){
			AmzBookItem newBook = new AmzBookItem();
			WebElement book = iteratorBooks.next();

			try {
				newBook.setImageThumb(StringUtils.defaultIfEmpty(book.findElement(By.className("s-image")).getAttribute("src"), "-"));
			} catch (NoSuchElementException e) {
				log.error(e.getMessage());
				newBook.setImageThumb("-");
			}
			log.info("ImageThumb: " + newBook.getImageThumb());

			try {
				newBook.setTitle(StringEscapeUtils.escapeEcmaScript(StringUtils.defaultIfEmpty(book.findElement(By.className("s-line-clamp-2")).getText(),"-")));
			} catch (NoSuchElementException e){
				log.error(e.getMessage());
				newBook.setTitle("-");
			}
			log.info("Title: " + newBook.getTitle());

			try {
				newBook.setDetails(StringEscapeUtils.escapeEcmaScript(StringUtils.defaultIfEmpty(book.findElements(By.cssSelector(".a-row.a-size-base.a-color-secondary")).get(0)
						.findElement(By.cssSelector(".a-row")).getText(), "-")));
			} catch (NoSuchElementException e){
				log.error(e.getMessage());
				newBook.setDetails("-");
			}
			log.info("BookDetails: " + newBook.getDetails());

			try {
				newBook.setRating(StringUtils.defaultIfEmpty(book.findElement(By.cssSelector(".a-section.a-spacing-none.a-spacing-top-micro"))
						.findElement(By.cssSelector("span")).getAttribute("aria-label"),"-"));
			} catch (NoSuchElementException e){
				log.error(e.getMessage());
				newBook.setRating("-");
			}
			log.info("Rating: " + newBook.getRating());

			try {
				newBook.setSource(StringUtils.defaultIfEmpty(book.findElement(By.cssSelector(".a-section.a-spacing-none.a-spacing-top-small")).findElement(By.cssSelector("a")).getText(), "-"));
			} catch (NoSuchElementException e){
				newBook.setSource("-");
				log.error(e.getMessage());
			}
			log.info("Source: " + newBook.getSource());

			try {
				newBook.setPrice(StringUtils.defaultIfEmpty(book.findElement(By.cssSelector(".a-section.a-spacing-none.a-spacing-top-small")).findElement(By.className("a-offscreen")).getAttribute("innerHTML"),"-"));
			} catch (NoSuchElementException e){
				log.error(e.getMessage());
				newBook.setPrice("-");
			}
			log.info("Price: " + newBook.getPrice() + "\n ---");

			books.add(newBook);
		}

		Thread.sleep(5000);
		driver.close();
		driver.quit();


		if (DBUtil.getConnection() != null) {
			for(AmzBookItem amzItem : books ) {
				DBUtil.insertData(DBUtil.getConnection(), amzItem);
			}
		} else {
			log.error("DB connection failed!");
		}
	}
}
