package com.gmail.theslavahero.ai.configuration;

import com.gmail.theslavahero.ai.properties.ChromedriverProperties;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Configuration
public class ChromeOptionsConfiguration {

    private final ChromedriverProperties chromedriverProperties;

    public ChromeOptionsConfiguration(ChromedriverProperties chromedriverProperties) {
        this.chromedriverProperties = chromedriverProperties;
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromedriverProperties.getPath());
    }

    @Bean
    public ChromeOptions options() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", Collections.singletonMap("intl.accept_languages", "en,en_US"));
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito");

        options.setHeadless(chromedriverProperties.isHeadless());
        options.addArguments("--log-level=3");
        options.addArguments("--silent");

        options.addArguments("--disable-setuid-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--window-position=0,0");
        options.addArguments("--ignore-certifcate-errors");
        options.addArguments("--ignore-certifcate-errors-spki-list");

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("w3c", false);
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
//        options.setBinary("/app/.apt/usr/bin/google-chrome");

        return options;
    }
}
