package com.united.controller;

import com.united.entity.Friendship;
import com.united.entity.Post;
import com.united.entity.PostUserTagg;
import com.united.entity.User;
import com.united.service.FriendshipService;
import com.united.service.PostService;
import com.united.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Log4j2
public class ProfileController {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final PostService postService;
    private static String friends = "friends";

    @GetMapping("/profile/{nickname}")
    public String profilePage(@PathVariable String nickname,
                              Authentication authentication,
                              @RequestParam(required = false) String filterType,
                              @RequestParam(required = false) String userNickname,
                              Model model) {
        String nicknameAuthenticatedUser = authentication.getName();
        User authenticatedUser = userService.getUserByNickname(nicknameAuthenticatedUser);
        User user = userService.getUserByNickname(nickname);

        model.addAttribute("user", user);
        model.addAttribute("authenticatedUser", authenticatedUser);
        model.addAttribute("isSuperAdmin", isSuperAdmin(authentication));


        List<Post> posts = filterPosts(filterType, user, userNickname);

        for (Post post : posts) {

            List<PostUserTagg> postUserTaggs = postService.getPostUserTaggsForPost(post.getId());

            post.setPostUserTaggs(postUserTaggs);
        }

        model.addAttribute("posts", posts);


        Friendship friendship = friendshipService.getFriendshipByIds(user.getId(), authenticatedUser.getId());
        model.addAttribute("friendship", friendship);

        log.info("User {} viewed the profile of {}", authenticatedUser.getNickname(), nickname);

        return "profile";
    }

    private List<Post> filterPosts(String filterType, User user, String userNickname) {
        List<Post> posts = new ArrayList<>();

        if ("own".equals(filterType)) {
            posts = postService.findAllByUserPostedIdInCurrentPage(user.getId(), user.getId());
        } else if (friends.equals(filterType)) {
            posts = postService.getAllWithoutPostsOwnerPage(user, user.getId());
        } else if ("custom".equals(filterType) && userNickname != null) {
            User customUser = userService.getUserByNickname(userNickname);
            posts.addAll(postService.findAllByUserPostedIdInCurrentPage(customUser.getId(), user.getId()));
        } else {
            posts = postService.getAllPosts(user.getId());
        }
        return posts;
    }

    @GetMapping("/requestSent")
    public String showRequestSent(Model model, Authentication authentication) {
        Long userId = userService.getUserByNickname(authentication.getName()).getId();
        List<User> requestSent = friendshipService.getSentRequest(userId);
        model.addAttribute("request", requestSent);
        log.info("User {} viewed their sent friend requests", authentication.getName());
        return "requestSent";
    }

    @GetMapping("/friends/{nickname}")
    public String showFriends(@PathVariable String nickname, Model model, Authentication authentication) {
        User user = userService.getUserByNickname(nickname);
        User authUser = userService.getUserByNickname(authentication.getName());
        List<User> userFriends;
        if (authUser.getId().equals(user.getId())) {
            userFriends = friendshipService.getFriends(authUser.getId());
        } else {
            userFriends = friendshipService.getFriends(user.getId());
        }
        model.addAttribute("friends", userFriends);
        model.addAttribute("authUser", authUser);
        model.addAttribute("ownerPage", user);
        log.info("User {} viewed their friends list", authentication.getName());
        return friends;
    }

    @GetMapping("/requestReceived")
    public String showRequestReceived(Model model, Authentication authentication) {
        Long userId = userService.getUserByNickname(authentication.getName()).getId();
        List<User> requestReceived = friendshipService.getRequestReceived(userId);
        model.addAttribute("request", requestReceived);
        log.info("User {} viewed their received friend requests", authentication.getName());
        return "requestReceived";
    }

    @GetMapping("/editProfile/{nickname}")
    public Object editProfileForm(@PathVariable String nickname, Authentication authentication, Model model) {
        User authUser = userService.getUserByNickname(authentication.getName());
        User pageOwner = userService.getUserByNickname(nickname);

        if (isSuperAdmin(authentication) || pageOwner.equals(authUser)) {
            model.addAttribute("user", pageOwner);
            return "editProfile"; // Назва HTML-шаблону
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "You cannot access this page.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }


    @PostMapping("/editProfile/{nickname}")
    public String saveEditedProfile(
            @PathVariable String nickname,
            @ModelAttribute User updatedUser,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User authUser = userService.getUserByNickname(authentication.getName());
        User pageOwner = userService.getUserByNickname(nickname);


        if (!isSuperAdmin(authentication) && !pageOwner.equals(authUser)) {
            redirectAttributes.addFlashAttribute("error", "You cannot update this profile.");
            return "redirect:/profile/" + nickname;
        }

        updatedUser.setNickname(pageOwner.getNickname());
        userService.updateUser(pageOwner.getId(), updatedUser);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/profile/" + nickname;
    }

    private boolean isSuperAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }


}
