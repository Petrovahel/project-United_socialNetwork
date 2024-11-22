package com.findme.service;

import com.findme.entity.User;
import com.findme.exception.UserNotFoundException;
import com.findme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
       return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User updateUser(Long userId, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(userId);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Оновлюємо поля
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

            existingUser.setDateLastActive(new Date());

            userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " does not exist.");
        }

        return updatedUser;
    }
}
