package com.united.service;

import com.united.entity.UserRole;
import com.united.entity.RoleType;
import com.united.exception.handler.NotFoundException;
import com.united.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public UserRole findByRole(RoleType role) {
        return roleRepository.findByRole(role).orElseThrow(() -> new NotFoundException("User with id " + role + " not found"));
    }
}
