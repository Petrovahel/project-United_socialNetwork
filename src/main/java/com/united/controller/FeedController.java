package com.united.controller;

import com.united.entity.Post;
import com.united.entity.User;
import com.united.service.FriendshipService;
import com.united.service.PostService;
import com.united.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class FeedController {
    private final FriendshipService friendshipService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/feed")
    public String showAllPostsByLastTwoWeek(Authentication authentication, Model model) {
        User authUser = userService.getUserByNickname(authentication.getName());
        List<User> friends = friendshipService.getFriends(authUser.getId());

        Date postDate = new Date();
        LocalDateTime postDateTime = postDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        List<Post> feedPosts = new ArrayList<>();

        for (User friend : friends) {
            for (Post post : postService.getAllPostsByUserPostedId(friend.getId())) {
                if (postDateTime.isAfter(twoWeeksAgo)) {
                    feedPosts.add(post);
                }
            }

        }
        if (feedPosts.isEmpty()) {
            model.addAttribute("message", "Your friend haven`t posted anything new");
        } else {
            feedPosts.sort((post1, post2) -> post2.getDatePosted().compareTo(post1.getDatePosted()));

            model.addAttribute("posts", feedPosts);
        }
        log.info("User views new posts of his friends");
        return "feed";
    }
}
