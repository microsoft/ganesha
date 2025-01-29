package com.microsoft.ganesha.plugins;

import java.util.Arrays;
import java.util.List;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;

public class CallerActivitiesPlugin {
    @DefineKernelFunction(name = "getActivities", description = "Gets recent and upcoming activities relating to the caller")
    public List<OrderActivities> getActivities() {
        List<OrderActivities> orders = Arrays.asList(
            // sample data - replace with API calls to fetch data
            new OrderActivities(12345, "Lisinopril", "100mg", "Once a week", "Pharmacy A", "Dr. Williams", "2022-11-22", Arrays.asList("2023-01-22", "2023-09-10"), "Booked"),
            new OrderActivities(12345, "Amlodipine", "20mg", "Once a day", "Pharmacy C", "Dr. Williams", "2022-11-11", Arrays.asList("2023-02-25"), "Closed")
        );

        return orders;
    }

    // public static void main(String[] args) throws JsonProcessingException {
    //     CallerActivitiesPlugin plugin = new CallerActivitiesPlugin();
    //     System.out.println(plugin.getActivities());
    // }
}
