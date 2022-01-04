package com.gmail.theslavahero.ai.exception;

/**
 * This exception is being thrown in case if an element was not found on a webpage. If so, it is most probably that xpath/
 * cssSelector has changed. To fix it, you have to go to the html of the document and copy new xpath/cssSelector.
 * <p>
 * Example:
 * <pre>
 * WebElement seeAllButton = findWebElementBy(webDriver, SEE_ALL_ACTIVITIES_BUTTON)
 * .orElseThrow(() -> ElementNotFoundException.create("See all activity button"));
 * </pre>
 *
 * @author Dmitriy Lysko
 * @since 23/02/2021
 */
public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(String message) {
        super(message);
    }

    /**
     * @param what an element that was not found.
     * @return {@link ElementNotFoundException} with message.
     */
    public static ElementNotFoundException create(String what) {
        return new ElementNotFoundException(String.format("Not found: %s%n", what));
    }
}
