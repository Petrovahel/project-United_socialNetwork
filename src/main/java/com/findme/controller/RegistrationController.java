package com.findme.controller;

import com.findme.entity.RelationShipStatus;
import com.findme.entity.User;
import com.findme.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(){
        return "register";
    }

    @PostMapping("/save")
    public String saveUser(@RequestParam("first_name") String firstName,
                           @RequestParam("last_name") String lastName,
                           @RequestParam("nickname") String nickname,
                           @RequestParam("password") String password,
                           @RequestParam("phone") String phone,
                           @RequestParam("city") String city,
                           @RequestParam("country") String country,
                           @RequestParam("age") Integer age,
                           @RequestParam("religion") String religion,
                           @RequestParam("relation_ship")RelationShipStatus relationShipStatus){
        User user = User.builder().
                firstName(firstName).
                lastName(lastName).
                nickname(nickname).
                password(password).
                phone(phone).
                city(city).
                country(country).
                age(age).
                religion(religion).
                relationShip(relationShipStatus).
                build();
        userService.saveUser(user);
        return "redirect:/login";
    }
}
