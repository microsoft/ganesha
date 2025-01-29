package com.microsoft.ganesha.plugins;

import java.util.List;

public class OrderActivities {
    private int memberId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String pharmacy;
    private String prescribingDoctor;
    private String prescriptionDate;
    private List<String> refillDates;
    private String orderStatus;

    public OrderActivities(int memberId, String medicationName, String dosage, String frequency, String pharmacy, String prescribingDoctor, String prescriptionDate, List<String> refillDates, String orderStatus) {
        this.memberId = memberId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.pharmacy = pharmacy;
        this.prescribingDoctor = prescribingDoctor;
        this.prescriptionDate = prescriptionDate;
        this.refillDates = refillDates;
        this.orderStatus = orderStatus;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getPrescribingDoctor() {
        return prescribingDoctor;
    }

    public void setPrescribingDoctor(String prescribingDoctor) {
        this.prescribingDoctor = prescribingDoctor;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public List<String> getRefillDates() {
        return refillDates;
    }

    public void setRefillDates(List<String> refillDates) {
        this.refillDates = refillDates;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}