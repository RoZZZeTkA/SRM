package com.grigorev.srm.service;

import com.grigorev.srm.dao.DepartmentDAO;
import com.grigorev.srm.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentDAO departmentDAO;

    @Autowired
    public DepartmentService(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public List<Department> findAll() {
        return departmentDAO.findAll();
    }

    public Department findById(int id) {
        return departmentDAO.findById(id);
    }
}
