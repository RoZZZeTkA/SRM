package com.grigorev.srm.dao;

import com.grigorev.srm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM usr", new BeanPropertyRowMapper<>(User.class));
    }

    public User findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM usr WHERE id = ?", new BeanPropertyRowMapper<>(User.class), id);
    }

    public User findByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM usr WHERE username = ?", new BeanPropertyRowMapper<>(User.class), username);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public boolean existsByUsername(String username){
        return jdbcTemplate.queryForObject("SELECT EXISTS (SELECT * FROM usr WHERE username = ?)", Boolean.class, username);
    }

    public User save(User user) {
        jdbcTemplate.update("INSERT INTO usr (role, username, password) VALUES (?, ?, ?)",
                user.getRole().name(), user.getUsername(), user.getPassword());
        return findByUsername(user.getUsername());
    }
}
