package com.united.controller;

import com.united.entity.RelationshipStatus;
import com.united.entity.UserRole;
import com.united.entity.RoleType;
import com.united.entity.User;
import com.united.service.RoleService;
import com.united.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Controller
@AllArgsConstructor
@Log4j
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/register")
    public String showRegistrationPage() {
        log.info("User is not registered and has gone to the registration page.");
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(@RequestParam("first_name") String firstName,
                           @RequestParam("last_name") String lastName,
                           @RequestParam("nickname") String nickname,
                           @RequestParam("password") String password,
                           @RequestParam("phone") String phone,
                           @RequestParam("city") String city,
                           @RequestParam("country") String country,
                           @RequestParam("age") Integer age,
                           @RequestParam("religion") String religion,
                           @RequestParam("relation_ship") RelationshipStatus relationShipStatus) {

        String hashedPassword = userService.hashPassword(password);
        log.info("User`s password was success hashed");

        UserRole userRole = roleService.findByRole(RoleType.USER);

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .nickname(nickname)
                .password(hashedPassword)
                .phone(phone)
                .city(city)
                .country(country)
                .age(age)
                .religion(religion)
                .relationShip(relationShipStatus)
                .userRoles(userRoles)
                .build();

        userService.saveUser(user);
        log.info("User was success saved in the users table");

        return "redirect:/login";
    }
}

