package com.eapi.utils;

import com.microsoft.applicationinsights.boot.dependencies.apachecommons.lang3.RandomStringUtils;

public class StringOpsUtil {

    /**
     * Utility method generates unique random string
     *
     * @param prefix
     * @param length
     * @return String
     */
    public static String generateRandomString(String prefix, int length) {
        return prefix.concat(RandomStringUtils.randomAlphanumeric(length));
    }

    /**
     * Method sanitize user provided data for logging
     *
     * @param message
     * @return String
     */
    public static String encode(String message) {
        message = message.replace("\n", "_").replace("\r", "_").replace("\t", "_");
        return message;
    }
}
