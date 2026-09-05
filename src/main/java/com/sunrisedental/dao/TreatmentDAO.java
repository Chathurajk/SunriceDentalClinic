package com.sunrisedental.dao;

import com.sunrisedental.model.Treatment;
import com.sunrisedental.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO {
    public List<Treatment> findActive() throws Exception {
        return find(true);
    }

    public List<Treatment> findAll() throws Exception {
        return find(false);
    }

    public Treatment findById(int id) throws Exception {
        String sql = "SELECT * FROM treatments WHERE id=?";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return map(resultSet);
                }
            }
        }
        return null;
    }

    public void add(String name, String description, double charge) throws Exception {
        String sql = "INSERT INTO treatments(name, description, charge) VALUES(?, ?, ?)";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, charge);
            statement.executeUpdate();
        }
    }

    public void update(int id, String name, String description, double charge) throws Exception {
        String sql = "UPDATE treatments SET name=?, description=?, charge=? WHERE id=?";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, charge);
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

    public void deactivate(int id) throws Exception {
        String sql = "UPDATE treatments SET active=FALSE WHERE id=?";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private List<Treatment> find(boolean activeOnly) throws Exception {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM treatments "
                + (activeOnly ? "WHERE active=TRUE " : "")
                + "ORDER BY name";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                treatments.add(map(resultSet));
            }
        }
        return treatments;
    }

    private Treatment map(ResultSet resultSet) throws Exception {
        Treatment treatment = new Treatment();
        treatment.setId(resultSet.getInt("id"));
        treatment.setName(resultSet.getString("name"));
        treatment.setDescription(resultSet.getString("description"));
        treatment.setCharge(resultSet.getDouble("charge"));
        treatment.setActive(resultSet.getBoolean("active"));
        return treatment;
    }
}
