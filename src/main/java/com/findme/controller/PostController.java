package com.findme.controller;

import com.findme.entity.Message;
import com.findme.entity.Post;
import com.findme.service.MessageService;
import com.findme.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public String findAll(Model model){
        List<Post> posts = postService.findAll();
        model.addAttribute("post", posts);
        return "posts";

    }

    public Post getPostById(Long id){
        return postService.getPostById(id);
    }

    public Post savePost(Post post){
        return postService.savePost(post);
    }

    public void deletePost(Long id){
        postService.deletePost(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        Post post = postService.updatePost(id, updatedPost);
        return ResponseEntity.ok(post);
    }
}
