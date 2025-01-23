package com.eapi.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    /**
     * Date utility method to format date string into desired format
     *
     * @param inputDate
     * @param inputFormatter
     * @param outputFormatter
     * @return String
     */
    public static String convertDateFormat(String inputDate, DateTimeFormatter inputFormatter,
                                           DateTimeFormatter outputFormatter) {
        LocalDate outputDate = LocalDate.parse(inputDate, inputFormatter);
        return outputDate.format(outputFormatter);
    }

    /**
     * Date utility method to get past date minus no days as input
     *
     * @param inputDate
     * @param inputFormatter
     * @param outputFormatter
     * @param noOfDays
     * @return String
     */
    public static String findPastDateByDays(String inputDate, DateTimeFormatter inputFormatter,
                                            DateTimeFormatter outputFormatter, long noOfDays) {
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);
        LocalDate outputDate = date.minusDays(noOfDays);
        return outputDate.format(outputFormatter);
    }

    /**
     * Date manipulation utility method to format date with local time string into
     * desired format
     *
     * @param inputDate
     * @param inputFormatter
     * @param outputFormatter
     * @return String
     */
    public static String convertDateTimeFormat(String inputDate, DateTimeFormatter inputFormatter,
                                               DateTimeFormatter outputFormatter) {
        LocalTime localTime = LocalTime.now();
        LocalDate outputDate = LocalDate.parse(inputDate, inputFormatter);
        return outputDate.atTime(localTime).format(outputFormatter);
    }

    /**
     * Datetime manipulation utility method to format datetime string into desired
     * format
     *
     * @param inputDate
     * @param inputFormatter
     * @param outputFormatter
     * @return String
     */
    public static String convertDateTimeFormatWithGivenTime(String inputDate, DateTimeFormatter inputFormatter,
                                                            DateTimeFormatter outputFormatter) {
        LocalDateTime outputDate = LocalDateTime.parse(inputDate, inputFormatter);
        return outputDate.format(outputFormatter);
    }

    /**
     * Method returns current timestamp
     *
     * @return long
     */
    public static long getCurrentEpochTimestamp() {
        LocalDateTime dateNow = LocalDateTime.now();
        ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        return dateNow.toEpochSecond(zoneOffset);
    }

    /**
     * Method converts given date string in format to LocalDateTime object
     *
     * @param inputDate
     * @param formatter
     * @return LocalDateTime
     */
    public static LocalDateTime convertToLocalDateTime(String inputDate, DateTimeFormatter formatter) {
        LocalDateTime dateTime = LocalDateTime.parse(inputDate, formatter);
        return dateTime;
    }

    /**
     * Method converts given LocalDateTime object in String format
     *
     * @param inputDate
     * @param formatter
     * @return LocalDateTime
     */
    public static String convertLocalDateTimeToString(LocalDateTime inputDate, DateTimeFormatter formatter) {
        String dateTime = inputDate.format(formatter);
        return dateTime;
    }
    
    /*public static void main(String[] args) {
    	String str = "4/3/2024 11:59:59.000000 AM";
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:m:s.SSSSSS a"); 
    	LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
    	
    	System.out.println("DATE :: "+dateTime);
    }*/
}
