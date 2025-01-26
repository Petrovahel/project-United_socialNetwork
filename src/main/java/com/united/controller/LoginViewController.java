package com.united.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
@Log4j
public class LoginViewController {

    @GetMapping("/login")
    public String loginPage() {
        log.info("Showing the login page");
        return "login";
    }

}

