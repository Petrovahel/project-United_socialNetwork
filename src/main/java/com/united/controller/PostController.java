package com.united.controller;

import com.united.entity.Post;
import com.united.entity.PostUserTagg;
import com.united.entity.User;
import com.united.repository.PostUserTaggsRepository;
import com.united.service.FriendshipService;
import com.united.service.PostService;
import com.united.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Log4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final PostUserTaggsRepository postUserTaggsRepository;

    @GetMapping("/createNewPostForm/{nickname}")
    public String showCreatePostForm(@PathVariable String nickname, Model model, Authentication authentication) {
        if (nickname == null || nickname.isEmpty()) {
            throw new IllegalArgumentException("Nickname must be provided.");
        }

        List<User> friends = friendshipService.getFriends(userService.getUserByNickname(authentication.getName()).getId());

        model.addAttribute("friends", friends);
        model.addAttribute("nickname", nickname);

        log.info("User tries to create a new post for the page: " + nickname);

        return "createNewPostForm";
    }


    @PostMapping("/saveNewPost")
    public String savePost(@RequestParam String message,
                           @RequestParam String location,
                           @RequestParam String nickname,
                           @RequestParam List<Long> taggedFriends,
                           Authentication authentication, Model model) {

        User authUser = userService.getUserByNickname(authentication.getName());

        Post post = new Post();
        post.setMessage(message);
        post.setLocation(location);
        post.setUserPosted(authUser);

        if (authUser.getNickname().equals(nickname)) {
            post.setUserPagePosted(authUser);
        } else {
            post.setUserPagePosted(userService.getUserByNickname(nickname));
        }

        Post savedPost = postService.savePost(post);

        if (savedPost.getId() == null) {
            log.error("Post was not saved successfully.");
            model.addAttribute("error", "Error saving post. Please try again.");
            return "redirect:/createNewPostForm";
        }

        if (taggedFriends != null && !taggedFriends.isEmpty()) {
            List<User> taggedUsers = userService.getUsersByIds(taggedFriends);
            log.info("Tagged users: " + taggedUsers);

            model.addAttribute("taggedUsers", taggedUsers);

            for (User taggedUser : taggedUsers) {
                PostUserTagg tag = new PostUserTagg(post, taggedUser);
                postUserTaggsRepository.save(tag);
            }
        }

        log.info("Post successfully saved with ID: " + savedPost.getId());

        return "redirect:/profile/" + nickname;
    }

    @PostMapping("/deletePost/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long id, Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        Post post = postService.getPostById(id);
        User authUser = userService.getUserByNickname(authentication.getName());

        boolean isAdminOrSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN") || authority.getAuthority().equals("ROLE_SUPER_ADMIN"));

        if (isAdminOrSuperAdmin ||
                post.getUserPosted().getId().equals(authUser.getId()) ||
                post.getUserPagePosted().getId().equals(authUser.getId())) {

            postService.deletePostWithTags(id);
            response.put("status", "success");
            response.put("message", "Post deleted successfully.");
            return ResponseEntity.ok(response);
        }

        response.put("status", "error");
        response.put("message", "You cannot delete this post.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }


}
