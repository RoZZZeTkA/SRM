package com.grigorev.srm.dao;

import com.grigorev.srm.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DepartmentDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Department> findAll() {
        return jdbcTemplate.query("SELECT * FROM department", new BeanPropertyRowMapper<>(Department.class));
    }

    public Department findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM department where id = ?", new BeanPropertyRowMapper<>(Department.class), id);
    }
}
