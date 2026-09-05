package com.sunrisedental.model;
public class User {
    private int id;
    private String fullName,email,passwordHash,role,contactNumber;
    private boolean active;
    public int getId() {
        return id;
    }
    public void setId(int v) {
        id=v;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String v) {
        fullName=v;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String v) {
        email=v;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String v) {
        passwordHash=v;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String v) {
        role=v;
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
