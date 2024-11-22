package com.findme.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping ("/")
    public String home(){
        return "index";
    }

    @RequestMapping(path = "/test-ajax", method = RequestMethod.GET)
    public String testAjax(){
        return "index";
    }
}
