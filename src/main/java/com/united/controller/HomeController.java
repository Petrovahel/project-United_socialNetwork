package com.united.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails user, Model model) {

        if (user == null || user.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            model.addAttribute("accessDenied", true);
            return "index";
        }
        model.addAttribute("nickname", user.getUsername());
        return "profile";
    }
}
