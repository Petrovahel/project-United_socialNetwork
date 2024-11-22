package com.findme.service;

import com.findme.entity.Post;
import com.findme.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post getPostById(Long id){
        return postRepository.findById(id).orElse(null);
    }

    public Post savePost(Post post){
        return postRepository.save(post);
    }

    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    public Post updatePost(Long postId, Post updatedPost) {
        Optional<Post> existingPostOpt = postRepository.findById(postId);

        if (existingPostOpt.isPresent()) {
            Post existingPost = existingPostOpt.get();

            // Оновлюємо поля
            existingPost.setMessage(updatedPost.getMessage());
            existingPost.setDatePosted(updatedPost.getDatePosted());
            existingPost.setUserPosted(updatedPost.getUserPosted());

        } else {
            throw new IllegalArgumentException("Post with id " + postId + " does not exist.");
        }

        return updatedPost;
    }
}
