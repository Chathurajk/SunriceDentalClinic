package com.sunrisedental.dao;

import com.sunrisedental.util.DbConnection;
import com.sunrisedental.model.User;
import java.sql.*;

public class UserDAO {
    public User authenticate(String email,String passwordHash)throws SQLException {
        String q="SELECT * FROM users WHERE email=? AND password_hash=? AND active=TRUE";
        try(Connection c=DbConnection.getInstance().getConnection();
        PreparedStatement p=c.prepareStatement(q)) {
            p.setString(1,email);
            p.setString(2,passwordHash);
            try(ResultSet r=p.executeQuery()) {
                if(r.next())return map(r);
            }
        }
        return null;
    }
    private User map(ResultSet r)throws SQLException {
        User u=new User();
        u.setId(r.getInt("id"));
        u.setFullName(r.getString("full_name"));
        u.setEmail(r.getString("email"));
        u.setPasswordHash(r.getString("password_hash"));
        u.setRole(r.getString("role"));
        u.setContactNumber(r.getString("contact_number"));
        u.setActive(r.getBoolean("active"));
        return u;
    }
}
