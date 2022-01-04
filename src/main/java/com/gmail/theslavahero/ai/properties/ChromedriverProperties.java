package com.gmail.theslavahero.ai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dmitriy Lysko
 * @since 01/02/2021
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.chromedriver")
public class ChromedriverProperties {
    private String path;
    private boolean headless;
}