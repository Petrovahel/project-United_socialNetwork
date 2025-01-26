package com.united.service;

import com.united.entity.User;
import com.united.exception.handler.NotFoundException;
import com.united.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return userRepository.findByIdIn(ids);
    }

    public User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new NotFoundException("User with nickname " + nickname + " not found"));
    }

    public void updateLastActiveDate(String username) {
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        user.setDateLastActive(new Date());
        userRepository.save(user);
    }

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId).orElseThrow();


        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setCountry(updatedUser.getCountry());
        existingUser.setCity(updatedUser.getCity());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setRelationShip(updatedUser.getRelationShip());
        existingUser.setReligion(updatedUser.getReligion());
        existingUser.setSchool(updatedUser.getSchool());
        existingUser.setUniversity(updatedUser.getUniversity());


        userRepository.save(existingUser);

        return updatedUser;
    }


}
