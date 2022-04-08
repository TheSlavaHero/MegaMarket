package com.gmail.theslavahero.ai;

import com.gmail.theslavahero.ai.exception.ElementNotFoundException;
import com.gmail.theslavahero.ai.googleSheets.GoogleSheetController;
import com.gmail.theslavahero.ai.scraper.PriceScraper;
import com.gmail.theslavahero.ai.utils.RemoteWebDriverProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static com.gmail.theslavahero.ai.scraper.PriceScraper.SEARCH_BUTTON;
import static com.gmail.theslavahero.ai.scraper.PriceScraper.SHOP_BUTTON;
import static com.gmail.theslavahero.ai.utils.CommonUtils.randomLong;
import static com.gmail.theslavahero.ai.utils.WebDriverUtils.clickOnElement;
import static com.gmail.theslavahero.ai.utils.WebDriverUtils.findWebElementBy;

@Slf4j
@Service
@EnableScheduling
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class Runner {

    private final ChromeOptions chromeOptions;
    private final PriceScraper scraper;
    private final GoogleSheetController sheetController;
    private static final List<String> RANGE = List.of("Dairy!C2:C997", "Snacks!C2:C997");

    @Scheduled(cron = "0 0 10 * * *")
    public void run() {
        try (RemoteWebDriverProvider launcher = new RemoteWebDriverProvider(chromeOptions)) {
            WebDriver webDriver = launcher.getWebDriver();
            for (String range : RANGE) {
                List<String> productNames = sheetController.getAllProductNames(range);
                List<String> prices = new java.util.ArrayList<>(Collections.emptyList());

                webDriver.get("https://megamarket.ua");
                WebDriverWait wait = new WebDriverWait(webDriver, 20, 1000);

                wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_BUTTON));
                WebElement shopButton = findWebElementBy(webDriver, SHOP_BUTTON)
                        .orElseThrow(() -> ElementNotFoundException.create("Shop button"));
                clickOnElement(shopButton, new Actions(webDriver), randomLong(300, 500));

                int number = 1;
                for (String productName : productNames) {
                    log.info("№" + number);
                    String price = scraper.getPrice(webDriver, wait, productName);
                    prices.add(price);
                    number++;
                }
                sheetController.writeAllPrices(prices, range);
            }

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
