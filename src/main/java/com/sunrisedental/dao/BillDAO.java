package com.sunrisedental.dao;

import com.sunrisedental.model.Bill;
import com.sunrisedental.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BillDAO {
    public synchronized String generateBillNumber(String date) throws Exception {
        String prefix = "BILL-" + date.replace("-", "");
        String sql = "SELECT MAX(CAST(SUBSTRING_INDEX(bill_number, '-', -1) AS UNSIGNED)) "
                + "FROM bills WHERE bill_number LIKE ?";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, prefix + "-%");

            try (ResultSet resultSet = statement.executeQuery()) {
                int next = 1;
                if (resultSet.next() && resultSet.getObject(1) != null) {
                    next = resultSet.getInt(1) + 1;
                }
                return prefix + "-" + String.format("%03d", next);
            }
        }
    }

    public Bill create(int appointmentId, String billNumber, double treatmentCharge,
                       double consultationFee) throws Exception {
        double total = treatmentCharge + consultationFee;
        String sql = "INSERT INTO bills "
                + "(bill_number, appointment_id, treatment_charge, consultation_fee, total_amount) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, billNumber);
            statement.setInt(2, appointmentId);
            statement.setDouble(3, treatmentCharge);
            statement.setDouble(4, consultationFee);
            statement.setDouble(5, total);
            statement.executeUpdate();

            Bill bill = new Bill();
            bill.setAppointmentId(appointmentId);
            bill.setBillNumber(billNumber);
            bill.setTreatmentCharge(treatmentCharge);
            bill.setConsultationFee(consultationFee);
            bill.setTotalAmount(total);
            bill.setStatus("UNPAID");

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    bill.setId(keys.getInt(1));
                }
            }
            return bill;
        }
    }

    public Bill findByAppointmentId(int appointmentId) throws Exception {
        String sql = "SELECT b.*, a.appointment_number, a.patient_name, "
                + "t.name treatment_name "
                + "FROM bills b "
                + "JOIN appointments a ON b.appointment_id = a.id "
                + "LEFT JOIN treatments t ON a.actual_treatment_id = t.id "
                + "WHERE b.appointment_id = ?";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointmentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return map(resultSet);
                }
            }
        }
        return null;
    }

    public java.util.List<Bill> findRecent(int limit) throws Exception {
        java.util.List<Bill> bills = new java.util.ArrayList<>();
        String sql = "SELECT b.*, a.appointment_number, a.patient_name, "
                + "t.name treatment_name "
                + "FROM bills b "
                + "JOIN appointments a ON b.appointment_id = a.id "
                + "LEFT JOIN treatments t ON a.actual_treatment_id = t.id "
                + "ORDER BY b.created_at DESC LIMIT ?";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bills.add(map(resultSet));
                }
            }
        }
        return bills;
    }

    private Bill map(ResultSet resultSet) throws Exception {
        Bill bill = new Bill();
        bill.setId(resultSet.getInt("id"));
        bill.setBillNumber(resultSet.getString("bill_number"));
        bill.setAppointmentId(resultSet.getInt("appointment_id"));
        bill.setAppointmentNumber(resultSet.getString("appointment_number"));
        bill.setPatientName(resultSet.getString("patient_name"));
        bill.setTreatmentName(resultSet.getString("treatment_name"));
        bill.setTreatmentCharge(resultSet.getDouble("treatment_charge"));
        bill.setConsultationFee(resultSet.getDouble("consultation_fee"));
        bill.setTotalAmount(resultSet.getDouble("total_amount"));
        bill.setStatus(resultSet.getString("status"));
        bill.setCreatedAt(resultSet.getString("created_at"));
        return bill;
    }
}
