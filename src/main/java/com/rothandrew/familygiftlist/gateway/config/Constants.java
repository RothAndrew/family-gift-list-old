package com.rothandrew.familygiftlist.gateway.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ERROR_KEY_UNABLE_TO_GET_CURRENT_USER = "unabletogetcurrentuser";
    public static final String ERROR_KEY_ENTITY_NOT_FOUND = "entitynotfound";
    public static final String ERROR_KEY_NOT_ALLOWED_TO_MODIFY_ENTITY = "notallowedtomodifyentity";

    private Constants() {
    }
}
