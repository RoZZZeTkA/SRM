package com.grigorev.srm.service;

import com.grigorev.srm.dao.UserDAO;
import com.grigorev.srm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDAO userDAO;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User findById(int id) {
        return userDAO.findById(id);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User save(User user) {
        if (user.getRole() == null ||
            user.getUsername() == null ||
            user.getPassword() == null) {
            throw new IllegalStateException("Some fields are null " + user);
        }
        if (userDAO.existsByUsername(user.getUsername())) {
            throw new IllegalStateException("Username " + user.getUsername() + " is already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userDAO.save(user);
        user.setPassword("");
        return user;
    }
}
