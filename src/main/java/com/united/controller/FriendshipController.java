package com.united.controller;

import com.united.entity.Friendship;
import com.united.entity.FriendshipStatus;
import com.united.entity.User;
import com.united.exception.FriendshipValidationException;
import com.united.handler.Validation;
import com.united.service.FriendshipService;
import com.united.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@Log4j2
public class FriendshipController {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final Validation validation;

    public FriendshipController(UserService userService, FriendshipService friendshipService, @Qualifier("validationAddition") Validation validation) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.validation = validation;
    }

    @PostMapping("/removeFriendship/{friendId}")
    public ResponseEntity<String> removeFriendship(@PathVariable Long friendId, Authentication authentication) {
        Long userId = userService.getUserByNickname(authentication.getName()).getId();

        try {
            friendshipService.removeFriendship(userId, friendId);
            log.info("Friendship with {} was successfully deleted", userService.getUserById(friendId).getNickname());
            return ResponseEntity.ok("Friend removed successfully");
        } catch (Exception e) {
            log.error("Error deleting friendship with {}: {}", userService.getUserById(friendId).getNickname(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You cannot remove this friend because your friendship lasts less than 3 days.");
        }
    }

    @PostMapping("/removeRequest/{friendId}")
    public ResponseEntity<String> removeRequest(@PathVariable Long friendId, Authentication authentication) {
        Long userId = userService.getUserByNickname(authentication.getName()).getId();
        friendshipService.removeRequest(userId, friendId);
        log.info("Request was successfully removed");
        return ResponseEntity.ok("Request removed successfully");
    }

    @PostMapping("/acceptFriend/{friendId}")
    public ResponseEntity<String> acceptFriend(@PathVariable Long friendId, Authentication authentication) {
        Long userId = userService.getUserByNickname(authentication.getName()).getId();
        friendshipService.acceptFriend(userId, friendId);
        log.info("User {} was successfully accepted as a friend", userService.getUserById(friendId).getNickname());
        return ResponseEntity.ok("Accept friend successfully");
    }

    @PostMapping("/addRequestSent/{userId}")
    public ResponseEntity<String> addNewRequest(@PathVariable Long userId, Authentication authentication, Model model) {
        User authenticatedUser = userService.getUserByNickname(authentication.getName());

        try {
            boolean isValid = validation.check(userId, authenticatedUser.getId());
            if (!isValid) {
                log.warn("Validation failed for user {}", authenticatedUser.getNickname());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed");
            }

            Friendship friendship = Friendship.builder()
                    .userFrom(userService.getUserById(authenticatedUser.getId()))
                    .userTo(userService.getUserById(userId))
                    .status(FriendshipStatus.REQUEST_SENT)
                    .build();
            friendshipService.addNewRequest(friendship);

            log.info("The request was successfully sent to {}", userService.getUserById(userId).getNickname());
            model.addAttribute(userId);
            return ResponseEntity.ok("The request was successfully sent");

        } catch (FriendshipValidationException e) {
            log.error("The request was not sent to {}: {}", userService.getUserById(userId).getNickname(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed");
        }
    }

    @ExceptionHandler(FriendshipValidationException.class)
    public ResponseEntity<String> handleValidationException(FriendshipValidationException e) {
        log.error("FriendshipValidationException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}




