package com.hjf.oa.biz;

import com.hjf.oa.entity.Employee;

public interface GlobalBiz {

    Employee login(String sn, String password);
    void changePassword(Employee employee);

}
