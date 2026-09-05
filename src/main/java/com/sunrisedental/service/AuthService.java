package com.sunrisedental.service;

import com.sunrisedental.dao.UserDAO;
import com.sunrisedental.model.User;
import com.sunrisedental.util.PasswordUtil;

public class AuthService {
    private final UserDAO dao;
    public AuthService() {
        dao=new UserDAO();
    }
    public AuthService(UserDAO dao) {
        this.dao=dao;
    }
    public User login(String email,String password)throws Exception {
        return dao.authenticate(email,PasswordUtil.sha256(password));
    }
}
