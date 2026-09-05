package com.sunrisedental.service;

import com.sunrisedental.dao.AppointmentDAO;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.util.ValidationUtil;

import java.time.LocalTime;

public class AppointmentService {
    private final AppointmentDAO dao;
    public AppointmentService() {
        dao=new AppointmentDAO();
    }
    public AppointmentService(AppointmentDAO dao) {
        this.dao=dao;
    }
    public Appointment create(Appointment a)throws Exception {
        validate(a);
        a.setAppointmentNumber(dao.generateAppointmentNumber(a.getAppointmentDate()));
        if(!dao.isAvailable(a.getDentistId(),a.getAppointmentDate(),a.getAppointmentTime(),0))throw new IllegalArgumentException("The selected dentist is already booked for this date and time.");
        return dao.save(a);
    }
    public void update(Appointment a)throws Exception {
        validate(a);
        if(!dao.isAvailable(a.getDentistId(),a.getAppointmentDate(),a.getAppointmentTime(),a.getId()))throw new IllegalArgumentException("The selected dentist is already booked for this date and time.");
        dao.update(a);
    }
    private void validate(Appointment a) {
        ValidationUtil.required(a.getPatientName(),"Patient name");
        if(!ValidationUtil.isValidEmail(a.getPatientEmail()))throw new IllegalArgumentException("Please enter a valid patient email.");
        ValidationUtil.required(a.getAddress(),"Address");
        ValidationUtil.required(a.getContactNumber(),"Contact number");
        ValidationUtil.appointmentDate(a.getAppointmentDate());
        ValidationUtil.required(a.getAppointmentTime(),"Appointment time");
        try {
            LocalTime.parse(a.getAppointmentTime().length() == 5
                    ? a.getAppointmentTime() + ":00"
                    : a.getAppointmentTime());
        } catch (Exception e) {
            throw new IllegalArgumentException("Please enter a valid appointment time.");
        }
        if(a.getDentistId()<=0)throw new IllegalArgumentException("Please select a dentist.");
        if(a.getRequestedTreatmentId()<=0)throw new IllegalArgumentException("Please select a treatment or visit reason.");
    }



}
