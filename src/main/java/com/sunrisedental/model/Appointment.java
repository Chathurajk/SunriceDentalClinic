package com.sunrisedental.model;
public class Appointment {
    private int id,dentistId,requestedTreatmentId,actualTreatmentId;
    private String appointmentNumber,patientName,patientEmail,address,contactNumber,appointmentDate,appointmentTime,status,notes,dentistName,treatmentName,requestedTreatmentName;
    public int getId() {
        return id;
    }
    public void setId(int v) {
        id=v;
    }
    public String getAppointmentNumber() {
        return appointmentNumber;
    }
    public void setAppointmentNumber(String v) {
        appointmentNumber=v;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String v) {
        patientName=v;
    }
    public String getPatientEmail() {
        return patientEmail;
    }
    public void setPatientEmail(String v) {
        patientEmail=v;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String v) {
        address=v;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String v) {
        contactNumber=v;
    }
    public int getDentistId() {
        return dentistId;
    }
    public void setDentistId(int v) {
        dentistId=v;
    }
    public int getRequestedTreatmentId() {
        return requestedTreatmentId;
    }
    public void setRequestedTreatmentId(int v) {
        requestedTreatmentId=v;
    }
    public int getActualTreatmentId() {
        return actualTreatmentId;
    }
    public void setActualTreatmentId(int v) {
        actualTreatmentId=v;
    }
    public String getRequestedTreatmentName() {
        return requestedTreatmentName;
    }

    public void setRequestedTreatmentName(String v) {
        requestedTreatmentName=v;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(String v) {
        appointmentDate=v;
    }
    public String getAppointmentTime() {
        return appointmentTime;
    }
    public void setAppointmentTime(String v) {
        appointmentTime=v;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String v) {
        status=v;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String v) {
        notes=v;
    }
    public String getDentistName() {
        return dentistName;
    }
    public void setDentistName(String v) {
        dentistName=v;
    }
    public String getTreatmentName() {
        return treatmentName;
    }
    public void setTreatmentName(String v) {
        treatmentName=v;
    }
}
