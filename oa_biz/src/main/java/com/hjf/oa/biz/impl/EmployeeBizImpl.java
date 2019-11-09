package com.hjf.oa.biz.impl;

import com.hjf.oa.biz.EmployeeBiz;
import com.hjf.oa.dao.EmployeeDao;
import com.hjf.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("employeeBiz")
public class EmployeeBizImpl implements EmployeeBiz {

    @Autowired
    private EmployeeDao employeeDao;

    public void add(Employee employee) {
        employee.setPassword("000000");
        employeeDao.insert(employee);
    }

    public void edit(Employee employee) {
        employeeDao.update(employee);
    }

    public void remove(String sn) {
        employeeDao.delete(sn);
    }

    public Employee find(String sn) {
        return employeeDao.select(sn);
    }

    public List<Employee> findAll() {
        return employeeDao.selectAll();
    }
}
