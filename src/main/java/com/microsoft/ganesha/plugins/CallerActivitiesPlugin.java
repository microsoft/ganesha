package com.microsoft.ganesha.plugins;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

public class CallerActivitiesPlugin {
    public String getActivities() throws JsonProcessingException {
        List<OrderActivities> orders = Arrays.asList(
            // sample data - replace with API calls to fetch data
            new OrderActivities(12345, "Lisinopril", "100mg", "Once a week", "Pharmacy A", "Dr. Williams", "2022-11-22", Arrays.asList("2023-01-22", "2023-09-10"), "Booked"),
            new OrderActivities(12345, "Amlodipine", "20mg", "Once a day", "Pharmacy C", "Dr. Williams", "2022-11-11", Arrays.asList("2023-02-25"), "Closed")
        );

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(orders);
    }

    public static void main(String[] args) throws JsonProcessingException {
        CallerActivitiesPlugin plugin = new CallerActivitiesPlugin();
        System.out.println(plugin.getActivities());
    }
}
