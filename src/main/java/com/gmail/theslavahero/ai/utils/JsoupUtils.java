package com.gmail.theslavahero.ai.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

/**
 * @author Yevhenii Filatov
 * @since 11/27/20
 **/

public final class JsoupUtils {

    private JsoupUtils() {
        throw new UnsupportedOperationException("utility class");
    }

    public static String absUrlFromHref(Element element) {
        return absUrl(element, "href");
    }

    public static String absUrlFromSrc(Element element) {
        return absUrl(element, "src");
    }

    public static String absUrl(Element element, String attribute) {
        if (element != null
                && StringUtils.isNotBlank(attribute)
                && element.hasAttr(attribute)
                && !element.attr(attribute).isEmpty()) {
            return element.absUrl(attribute);
        } else {
            return null;
        }
    }

    public static String[] toStringArray(Elements elements) {
        String[] strings = null;

        if (elements != null && !elements.isEmpty()) {
            strings = elements.stream()
                    .map(Element::text)
                    .map(StringUtils::normalizeSpace)
                    .filter(StringUtils::isNotEmpty)
                    .distinct()
                    .toArray(String[]::new);
        }

        return strings;
    }

    public static String[] toStringArray(Element element) {
        String[] strings = null;

        if (element != null) {
            strings = toStringArray(new Elements(element));
        }

        return strings;
    }

    public static List<String> toStringList(Elements elements) {
        List<String> strings = Collections.emptyList();

        if (elements != null && !elements.isEmpty()) {
            strings = elements.stream()
                    .map(Element::text)
                    .map(StringUtils::normalizeSpace)
                    .filter(StringUtils::isNotEmpty)
                    .distinct()
                    .collect(Collectors.toList());
        }

        return strings;
    }

    public static List<String> toStringList(Element element) {
        List<String> strings = Collections.emptyList();

        if (element != null) {
            strings = toStringList(new Elements(element));
        }

        return strings;
    }

    public static String[] toStringArrayFromTextNodes(Element element) {
        String[] strings = null;

        if (element != null) {
            strings = element.textNodes()
                    .stream()
                    .map(TextNode::text)
                    .map(String::trim)
                    .filter(StringUtils::isNotEmpty)
                    .toArray(String[]::new);
        }

        return strings;
    }

    public static String text(Node node) {
        String text = null;
        if (node != null) {
            if (node instanceof TextNode) {
                text = StringUtils.normalizeSpace(((TextNode) node).text());
            } else if (node instanceof Element) {
                text = StringUtils.normalizeSpace(((Element) node).text());
            }
        }
        return text;
    }

    public static String text(Elements elements) {
        String text = "";
        if (elements != null && !elements.isEmpty()) {
            text = elements.text().trim();
        }
        return text;
    }

    public static String email(Element emailElement, String emailAttribute) {
        String email = "";
        if (emailElement != null && StringUtils.isNotBlank(emailAttribute)) {
            email = StringUtils.remove(emailElement.attr(emailAttribute), "mailto:").trim().toLowerCase();
        }
        if (email.contains("?")) {
            email = StringUtils.substringBefore(email, "?");
        }
        return email;
    }

    public static String email(Element emailElement) {
        return email(emailElement, "href");
    }

    public static List<TextNode> textNodes(Element element) {
        List<TextNode> textNodes = new ArrayList<>(element.textNodes());
        textNodes.removeIf(TextNode::isBlank);
        return textNodes;
    }

    public static Elements splitMarkup(Element element, String regex) {
        Objects.requireNonNull(regex);
        if (element == null) {
            return new Elements();
        }
        final String markup = element.html();
        return Arrays.stream(markup.split(regex))
                .map(item -> Jsoup.parse(item).select("body").first())
                .filter(e -> StringUtils.isNotBlank(text(e)))
                .peek(e -> e.setBaseUri(element.baseUri()))
                .collect(toCollection(Elements::new));
    }

    public static Elements getElementsBeforeNextBlock(Elements elements, String selector) {
        if (elements.size() == 1) {
            Elements childrenElements = getChildrenElements(elements);
            if (childrenElements.size() > 1) {
                elements = childrenElements;
            }
        }
        Elements result = new Elements();
        for (Element element : elements) {
            if (element.select(selector).isEmpty()) {
                result.add(element);
            } else {
                break;
            }
        }
        return result;
    }

    public static Elements getChildrenElements(Elements elements) {
        return elements.stream()
                .map(Element::children)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(Elements::new));
    }

    public static String getAttribute(Element element, String attr) {
        if (Objects.nonNull(element) && element.hasAttr(attr)) {
            return StringUtils.normalizeSpace(element.attr(attr));
        }
        return StringUtils.EMPTY;
    }

    public static String getValueAttribute(Element element) {
        return getAttribute(element, "value");
    }
}
