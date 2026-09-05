package com.sunrisedental.model;
public class Dentist {
    private int id;
    private String name,specialization,contactNumber;
    private boolean active;
    public int getId() {
        return id;
    }
    public void setId(int v) {
        id=v;
    }
    public String getName() {
        return name;
    }
    public void setName(String v) {
        name=v;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String v) {
        specialization=v;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String v) {
        contactNumber=v;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean v) {
        active=v;
    }
}
