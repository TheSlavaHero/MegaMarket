package com.gmail.theslavahero.ai.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.gmail.theslavahero.ai.utils.ChromeDriverLauncher;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viacheslav_Yakovenko
 * @since 03.08.2021
 */
@Configuration
public class ChromeDriverLauncherConfiguration {

    private final List<ChromeDriverLauncher> chromeDriverLaunchers = new ArrayList<>();

    @Bean
    public List<ChromeDriverLauncher> ChromeDriverLaunchers() {
        return chromeDriverLaunchers;
    }

    @PreDestroy
    public void destroy() {
        for (ChromeDriverLauncher chromeDriverLauncher : chromeDriverLaunchers) {
            chromeDriverLauncher.close();
        }
    }
}
