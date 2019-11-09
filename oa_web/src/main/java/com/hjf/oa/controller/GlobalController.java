package com.hjf.oa.controller;

import com.hjf.oa.biz.GlobalBiz;
import com.hjf.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller("globalController")
public class GlobalController {

    @Autowired
    private GlobalBiz globalBiz;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(HttpSession session, @RequestParam String sn, @RequestParam String password) {

        Employee employee = globalBiz.login(sn, password);
        if (employee == null) {
            return "redirect:to_login";
        }
        session.setAttribute("employee", employee);
        return "redirect:top";
    }

    @RequestMapping("/top")
    public String top(HttpSession session) {
        Employee employee = (Employee) session.getAttribute("employee");
        return "redirect:self";
    }

    @RequestMapping("/self")
    public String self() {
        return "self";
    }

    @RequestMapping("/quit")
    public String quit(HttpSession session) {

        session.setAttribute("employee", null);
        return "redirect:to_login";
    }

    @RequestMapping("/to_change_password")
    public String toChangePassword() {
        return "change_password";
    }

    @RequestMapping("/change_password")
    public String changePassword(HttpSession session, @RequestParam String oldPassword, @RequestParam String newPassword1, @RequestParam String newPassword2) {

        Employee employee = (Employee) session.getAttribute("employee");
        if (employee.getPassword().equals(oldPassword)) {
            if (newPassword1.equals(newPassword2)) {
                employee.setPassword(newPassword1);
                globalBiz.changePassword(employee);
                return "redirect:quit";
            }
        }
        return "redirect:to_change_password";
    }
}
