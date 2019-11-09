package com.hjf.oa.biz.impl;

import com.hjf.oa.biz.DepartmentBiz;
import com.hjf.oa.dao.DepartmentDao;
import com.hjf.oa.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("departmentBiz")
public class DepartmentBizImpl implements DepartmentBiz {

    @Autowired
    private DepartmentDao departmentDao;

    public void add(Department department) {
        departmentDao.insert(department);
    }

    public void edit(Department department) {
        departmentDao.update(department);
    }

    public void remove(String sn) {
        departmentDao.delete(sn);
    }

    public Department find(String sn) {
        return departmentDao.select(sn);
    }

    public List<Department> findAll() {
        return departmentDao.selectAll();
    }
}
