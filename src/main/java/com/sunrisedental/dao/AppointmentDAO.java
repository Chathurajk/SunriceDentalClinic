package com.sunrisedental.dao;

import com.sunrisedental.util.DbConnection;
import com.sunrisedental.model.Appointment;
import java.sql.*;
import java.util.*;

public class AppointmentDAO {
    public boolean isAvailable(int dentistId,String date,String time,int excludedId)throws SQLException {
        String q="SELECT COUNT(*) FROM appointments WHERE dentist_id=? AND appointment_date=? AND appointment_time=? AND status<>'CANCELLED' AND id<>?";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q)) {
            p.setInt(1,dentistId);
            p.setDate(2,java.sql.Date.valueOf(date));
            p.setTime(3,toSqlTime(time));
            p.setInt(4,excludedId);
            try(ResultSet r=p.executeQuery()) {
                r.next();
                return r.getInt(1)==0;
            }
        }
    }
    public synchronized String generateAppointmentNumber(String date) throws SQLException {
        String prefix = "APP-" + date.replace("-", "");
        String q = "SELECT MAX(CAST(SUBSTRING_INDEX(appointment_number, '-', -1) AS UNSIGNED)) "
                + "FROM appointments WHERE appointment_number LIKE ?";
        try (Connection c = DbConnection.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(q)) {
            p.setString(1, prefix + "-%");
            try (ResultSet r = p.executeQuery()) {
                int next = 1;
                if (r.next() && r.getObject(1) != null) {
                    next = r.getInt(1) + 1;
                }
                return prefix + "-" + String.format("%03d", next);
            }
        }
    }

    public Appointment save(Appointment a)throws SQLException {
        String q="INSERT INTO appointments(appointment_number,patient_name,patient_email,address,contact_number,dentist_id,requested_treatment_id,appointment_date,appointment_time,notes) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1,a.getAppointmentNumber());
            p.setString(2,a.getPatientName());
            p.setString(3,a.getPatientEmail());
            p.setString(4,a.getAddress());
            p.setString(5,a.getContactNumber());
            p.setInt(6,a.getDentistId());
            p.setInt(7,a.getRequestedTreatmentId());
            p.setDate(8,java.sql.Date.valueOf(a.getAppointmentDate()));
            p.setTime(9,toSqlTime(a.getAppointmentTime()));
            p.setString(10,a.getNotes());
            p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()) {
                if(r.next())a.setId(r.getInt(1));
            }
            return a;
        }
    }
    public void update(Appointment a)throws SQLException {
        String q="UPDATE appointments SET patient_name=?,patient_email=?,address=?,contact_number=?,dentist_id=?,requested_treatment_id=?,appointment_date=?,appointment_time=?,notes=? WHERE id=?";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q)) {
            p.setString(1,a.getPatientName());
            p.setString(2,a.getPatientEmail());
            p.setString(3,a.getAddress());
            p.setString(4,a.getContactNumber());
            p.setInt(5,a.getDentistId());
            p.setInt(6,a.getRequestedTreatmentId());
            p.setDate(7,java.sql.Date.valueOf(a.getAppointmentDate()));
            p.setTime(8,toSqlTime(a.getAppointmentTime()));
            p.setString(9,a.getNotes());
            p.setInt(10,a.getId());
            p.executeUpdate();
        }
    }
    public void cancel(int id)throws SQLException {
        updateStatus(id,"CANCELLED");
    }
    public void complete(int id,int treatmentId)throws SQLException {
        String q="UPDATE appointments SET status='COMPLETED',actual_treatment_id=? WHERE id=?";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q)) {
            p.setInt(1,treatmentId);
            p.setInt(2,id);
            p.executeUpdate();
        }
    }
    private void updateStatus(int id,String status)throws SQLException {
        String q="UPDATE appointments SET status=? WHERE id=?";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q)) {
            p.setString(1,status);
            p.setInt(2,id);
            p.executeUpdate();
        }
    }
    public Appointment findById(int id)throws SQLException {
        String q="SELECT a.*,d.name dentist_name,rt.name requested_treatment_name,t.name treatment_name FROM appointments a JOIN dentists d ON a.dentist_id=d.id LEFT JOIN treatments rt ON a.requested_treatment_id=rt.id LEFT JOIN treatments t ON a.actual_treatment_id=t.id WHERE a.id=?";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q)) {
            p.setInt(1,id);
            try(ResultSet r=p.executeQuery()) {
                if(r.next())return map(r);
            }
        }
        return null;
    }
    public Appointment findByNumber(String n)throws SQLException {
        String q="SELECT a.*,d.name dentist_name,rt.name requested_treatment_name,t.name treatment_name FROM appointments a JOIN dentists d ON a.dentist_id=d.id LEFT JOIN treatments rt ON a.requested_treatment_id=rt.id LEFT JOIN treatments t ON a.actual_treatment_id=t.id WHERE a.appointment_number=?";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q)) {
            p.setString(1,n);
            try(ResultSet r=p.executeQuery()) {
                if(r.next())return map(r);
            }
        }
        return null;
    }
    public List<Appointment> findAll()throws SQLException {
        List<Appointment> list=new ArrayList<>();
        String q="SELECT a.*,d.name dentist_name,rt.name requested_treatment_name,t.name treatment_name FROM appointments a JOIN dentists d ON a.dentist_id=d.id LEFT JOIN treatments rt ON a.requested_treatment_id=rt.id LEFT JOIN treatments t ON a.actual_treatment_id=t.id ORDER BY a.appointment_date,a.appointment_time";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q);
        ResultSet r=p.executeQuery()) {
            while(r.next())list.add(map(r));
        }
        return list;
    }
    public int countToday() throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appointment_date=CURRENT_DATE";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public int countAll()throws SQLException {
        return count("");
    }
    public int count(String status)throws SQLException {
        String q=status.isBlank()?"SELECT COUNT(*) FROM appointments":"SELECT COUNT(*) FROM appointments WHERE status=?";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q)) {
            if(!status.isBlank())p.setString(1,status);
            try(ResultSet r=p.executeQuery()) {
                r.next();
                return r.getInt(1);
            }
        }
    }
    private Appointment map(ResultSet r)throws SQLException {
        Appointment a=new Appointment();
        a.setId(r.getInt("id"));
        a.setAppointmentNumber(r.getString("appointment_number"));
        a.setPatientName(r.getString("patient_name"));
        a.setPatientEmail(r.getString("patient_email"));
        a.setAddress(r.getString("address"));
        a.setContactNumber(r.getString("contact_number"));
        a.setDentistId(r.getInt("dentist_id"));
        a.setRequestedTreatmentId(r.getInt("requested_treatment_id"));
        a.setActualTreatmentId(r.getInt("actual_treatment_id"));
        a.setAppointmentDate(r.getString("appointment_date"));
        a.setAppointmentTime(r.getString("appointment_time"));
        a.setStatus(r.getString("status"));
        a.setNotes(r.getString("notes"));
        a.setDentistName(r.getString("dentist_name"));
        a.setTreatmentName(r.getString("treatment_name"));
        a.setRequestedTreatmentName(r.getString("requested_treatment_name"));
        return a;
    }
    private Time toSqlTime(String value) {
        if (value != null && value.length() == 5) {
            return Time.valueOf(value + ":00");
        }
        return Time.valueOf(value);
    }

}
