package com.sunrisedental.model;
public class Treatment {
    private int id;
    private String name,description;
    private double charge;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String v) {
        description=v;
    }
    public double getCharge() {
        return charge;
    }
    public void setCharge(double v) {
        charge=v;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean v) {
        active=v;
    }
}
