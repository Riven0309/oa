package com.hjf.oa.biz;

import com.hjf.oa.entity.Department;

import java.util.List;

public interface DepartmentBiz {

    void add(Department department);
    void edit(Department department);
    void remove(String sn);
    Department find(String sn);
    List<Department> findAll();
}
