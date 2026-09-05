package com.sunrisedental.dao;

import com.sunrisedental.model.Dentist;
import com.sunrisedental.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DentistDAO {
    public List<Dentist> findActive() throws Exception {
        return find(true);
    }

    public List<Dentist> findAll() throws Exception {
        return find(false);
    }

    public int countActive() throws Exception {
        String sql = "SELECT COUNT(*) FROM dentists WHERE active=TRUE";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public void add(String name, String specialization, String contactNumber) throws Exception {
        String sql = "INSERT INTO dentists(name,specialization,contact_number) VALUES(?,?,?)";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, specialization);
            statement.setString(3, contactNumber);
            statement.executeUpdate();
        }
    }

    public void deactivate(int id) throws Exception {
        String sql = "UPDATE dentists SET active=FALSE WHERE id=?";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private List<Dentist> find(boolean activeOnly) throws Exception {
        List<Dentist> dentists = new ArrayList<>();
        String sql = "SELECT * FROM dentists "
                + (activeOnly ? "WHERE active=TRUE " : "")
                + "ORDER BY active DESC, name";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Dentist dentist = new Dentist();
                dentist.setId(resultSet.getInt("id"));
                dentist.setName(resultSet.getString("name"));
                dentist.setSpecialization(resultSet.getString("specialization"));
                dentist.setContactNumber(resultSet.getString("contact_number"));
                dentist.setActive(resultSet.getBoolean("active"));
                dentists.add(dentist);
            }
        }
        return dentists;
    }
}
