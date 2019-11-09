package com.hjf.oa.biz;

import com.hjf.oa.entity.Employee;

import java.util.List;

public interface EmployeeBiz {

    void add(Employee employee);
    void edit(Employee employee);
    void remove(String sn);
    Employee find(String sn);
    List<Employee> findAll();
}
