package org.example.scraper;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class YahooFinanceScraper {
    private static final String BASE_URL = "https://finance.yahoo.com/markets/stocks/most-active/?start=0&count=196";

    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\geckoDriver\\geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);

        WebDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try (CSVWriter writer = new CSVWriter(new FileWriter("stock_data.csv"))) {
            String[] headers = {"Company Name", "Price", "Change %"};
            writer.writeNext(headers);

            driver.get(BASE_URL);

            // Handle the fucking cookie consent ( this is my first time scrapping and i didn't know about this shit )
            try {
                WebElement consentOverlay = wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector(".consent-overlay")));
                WebElement acceptButton = consentOverlay.findElement(By.cssSelector(".accept-all"));
                acceptButton.click();
                System.out.println("Accepted cookie consent.");
            } catch (Exception e) {
                System.out.println("No consent form displayed or failed to accept.");
            }

            String tableXPath = "/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(tableXPath)));

            for (int i = 1; i <= 196; i++) {
                try {
                    String companyNameXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[1]/span/div/a/div/span[2]", i);
                    String companyName = driver.findElement(By.xpath(companyNameXPath)).getText().trim();

                    String priceXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[2]/span/fin-streamer", i);
                    String price = driver.findElement(By.xpath(priceXPath)).getText().trim();

                    String changePercentXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[4]/span/fin-streamer", i);
                    String changePercent = driver.findElement(By.xpath(changePercentXPath)).getText().trim();

                    System.out.println("Company: " + companyName + " | Price: " + price + " | Change %: " + changePercent);

                    String[] data = {companyName, price, changePercent};
                    writer.writeNext(data);
                } catch (Exception e) {
                    System.err.println("Error processing row " + i + ": " + e.getMessage());
                }
            }

            System.out.println("Data extraction completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
