package com.gmail.theslavahero.ai.scraper;

import com.gmail.theslavahero.ai.exception.ElementNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.gmail.theslavahero.ai.utils.CommonUtils.randomLong;
import static com.gmail.theslavahero.ai.utils.ParsingUtils.toElement;
import static com.gmail.theslavahero.ai.utils.WebDriverUtils.*;

@Slf4j
@Configuration
public class PriceScraper {

    private static final By SEARCH_FIELD = By.cssSelector("form.search > input.input_search");
    public static final By SEARCH_BUTTON = By.cssSelector("form.search > button");
    private static final By PRICE_OF_PRODUCT = By.cssSelector("div.product_view div.price");
    public static final By SHOP_BUTTON = By.cssSelector("ul.top_line_ul > li");
    public static final By CHECK_PRODUCT = By.cssSelector("div.product_view");

    public String getPrice(WebDriver webDriver, WebDriverWait wait, String nameOfProduct) {
        log.info("Getting price for the product: " + nameOfProduct);
        try {
            getProductSite(webDriver, wait, nameOfProduct);
//            WebElement searchField = findWebElementBy(webDriver, CHECK_PRODUCT)
//                    .orElseThrow(() -> ElementNotFoundException.create("product was not found"));
            wait.until(ExpectedConditions.presenceOfElementLocated(CHECK_PRODUCT));
            List<WebElement> priceWebElement = webDriver.findElements(PRICE_OF_PRODUCT);
            if (priceWebElement.size() == 0) throw new ElementNotFoundException("Price of the product was not found");
            Element priceElement = toElement(getElementHtml(priceWebElement.get(0)));//outofboundsexception
            String price = priceElement.selectFirst("div.price").text();
            return price.substring(0, price.length() - 4);
        } catch (StaleElementReferenceException e) {
            log.warn("Failed to get price for the product. Retrying.");
            webDriver.get("https://megamarket.ua");
            return getPrice(webDriver, wait, nameOfProduct);
        } catch (ElementNotFoundException | TimeoutException e) {
            log.error("Could not find the price for the product: " + nameOfProduct);
            webDriver.get("https://megamarket.ua");
            return "-";
        }
    }

    private static void getProductSite(WebDriver webDriver, WebDriverWait wait, String nameOfProduct) {
        wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_FIELD));
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH_FIELD));
        wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
        WebElement searchField = findWebElementBy(webDriver, SEARCH_FIELD)
                .orElseThrow(() -> ElementNotFoundException.create("Search field"));
        WebElement searchButton = findWebElementBy(webDriver, SEARCH_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.create("Search button"));

        enterDataIntoTextFieldWithKeyboard(searchField, nameOfProduct);
        clickOnElement(searchButton, new Actions(webDriver), randomLong(300, 500));
    }

}
