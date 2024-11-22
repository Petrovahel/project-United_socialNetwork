package com.findme;

import com.findme.controller.UserController;
import com.findme.entity.RelationShipStatus;
import com.findme.entity.User;
import com.findme.repository.UserRepository;
import com.findme.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SpringBootApplication
public class FindmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindmeApplication.class, args);
    }

}
