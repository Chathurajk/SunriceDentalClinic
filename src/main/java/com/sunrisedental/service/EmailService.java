package com.sunrisedental.service;

import com.sunrisedental.model.Appointment;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.*;

public class EmailService {
    private final String sender="";
    private final String appPassword="";
    public void sendAppointmentConfirmation(Appointment a) {
        if(sender.startsWith("YOUR_")) {
            System.out.println("SMTP not configured; email skipped.");
            return;
        }
        Properties p=new Properties();
        p.put("mail.smtp.auth","true");
        p.put("mail.smtp.starttls.enable","true");
        p.put("mail.smtp.host","smtp.gmail.com");
        p.put("mail.smtp.port","587");
        Session s=Session.getInstance(p,new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender,appPassword);
            }
        }
        );
        try {
            Message m=new MimeMessage(s);
            m.setFrom(new InternetAddress(sender));
            m.setRecipients(Message.RecipientType.TO,InternetAddress.parse(a.getPatientEmail()));
            m.setSubject("Sunrise Dental Clinic - Appointment Confirmation");
            m.setText("Dear "+a.getPatientName()+",\n\nYour appointment is confirmed.\nAppointment: "+a.getAppointmentNumber()+"\nDentist: "+a.getDentistName()+"\nDate: "+a.getAppointmentDate()+"\nTime: "+a.getAppointmentTime()+"\n\nSunrise Dental Clinic");
            Transport.send(m);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
