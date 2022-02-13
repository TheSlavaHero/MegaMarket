package com.gmail.theslavahero.ai.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class RemoteWebDriverProvider implements AutoCloseable {

    private final URL remoteUrl;
    private final ChromeOptions options;
    private final List<WebDriver> webDrivers = new ArrayList<>();

    public RemoteWebDriverProvider(ChromeOptions options) {
        URL remoteUrl1;
        this.options = options;
        remoteUrl1 = null;
        try {
            remoteUrl1 = new URL("http://localhost:4444");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.remoteUrl = remoteUrl1;

    }

    public WebDriver getWebDriver() {
        WebDriver webDriver = new RemoteWebDriver(remoteUrl, options);
        webDrivers.add(webDriver);
        return webDriver;
    }

    @Override
    public void close() {
        webDrivers.forEach(webDriver -> {
            webDriver.close();
            webDriver.quit();
        });
    }
}
