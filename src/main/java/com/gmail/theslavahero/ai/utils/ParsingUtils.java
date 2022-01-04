package com.gmail.theslavahero.ai.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParsingUtils {

    public static Element toElement(String source) {
         return Jsoup.parse(source, "https://www.linkedin.com").body();//TODO: CHANGE
    }

    public static long toLong(String numbers) {
        return Long.parseLong(numbers);
    }

    public static String extractNumbers(String text) {
        return normalizeSpace(text.replaceAll("\\D+", ""));
    }

    public static String extractLetters(String text) {
        return normalizeSpace(text.replaceAll("\\d+|r+", ""));
    }

    public static String capitalizeFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
