package com.sunrisedental.dao;

import com.sunrisedental.model.DentistReport;
import com.sunrisedental.model.TreatmentReport;
import com.sunrisedental.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    public double getTotalRevenue() throws Exception {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bills";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getDouble(1);
        }
    }

    public double getTodayRevenue() throws Exception {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bills "
                + "WHERE DATE(created_at) = CURRENT_DATE";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getDouble(1);
        }
    }

    public List<TreatmentReport> getTreatmentReport() throws Exception {
        List<TreatmentReport> reports = new ArrayList<>();
        String sql = "SELECT t.name, COUNT(b.id) usage_count, "
                + "COALESCE(SUM(b.treatment_charge), 0) revenue "
                + "FROM treatments t "
                + "LEFT JOIN appointments a ON a.actual_treatment_id = t.id "
                + "LEFT JOIN bills b ON b.appointment_id = a.id "
                + "GROUP BY t.id, t.name ORDER BY usage_count DESC, t.name";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                TreatmentReport report = new TreatmentReport();
                report.setTreatmentName(resultSet.getString("name"));
                report.setUsageCount(resultSet.getInt("usage_count"));
                report.setRevenue(resultSet.getDouble("revenue"));
                reports.add(report);
            }
        }
        return reports;
    }

    public List<DentistReport> getDentistReport() throws Exception {
        List<DentistReport> reports = new ArrayList<>();
        String sql = "SELECT d.name, COUNT(a.id) appointment_count, "
                + "SUM(CASE WHEN a.status='COMPLETED' THEN 1 ELSE 0 END) completed_count "
                + "FROM dentists d "
                + "LEFT JOIN appointments a ON a.dentist_id = d.id "
                + "GROUP BY d.id, d.name ORDER BY appointment_count DESC, d.name";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                DentistReport report = new DentistReport();
                report.setDentistName(resultSet.getString("name"));
                report.setAppointmentCount(resultSet.getInt("appointment_count"));
                report.setCompletedCount(resultSet.getInt("completed_count"));
                reports.add(report);
            }
        }
        return reports;
    }
}
