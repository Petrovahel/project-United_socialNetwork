package com.united.service;

import com.united.entity.Post;
import com.united.entity.PostUserTagg;
import com.united.entity.User;
import com.united.exception.handler.NotFoundException;
import com.united.repository.PostRepository;
import com.united.repository.PostUserTaggsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostUserTaggsRepository postUserTaggsRepository;

    public Post getPostById(Long id) {
        return postRepository.findPostById(id).orElseThrow(() -> new NotFoundException("Post not found"));
    }


    public Post savePost(Post post) {
        return postRepository.save(post);
    }


    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void deletePostWithTags(Long postId) {
        // Видаляємо всі теги для поста
        postUserTaggsRepository.deleteAllByPostId(postId);

        // Тепер видаляємо сам пост
        postRepository.deleteById(postId);
    }

    public Post updatePost(Long postId, Post updatedPost) {
        Optional<Post> existingPostOpt = postRepository.findById(postId);

        if (existingPostOpt.isPresent()) {
            Post existingPost = existingPostOpt.get();

            existingPost.setMessage(updatedPost.getMessage());
            existingPost.setDatePosted(updatedPost.getDatePosted());
            existingPost.setUserPosted(updatedPost.getUserPosted());

        } else {
            throw new IllegalArgumentException("Post with id " + postId + " does not exist.");
        }

        return updatedPost;
    }

    public List<PostUserTagg> getPostUserTaggsForPost(Long postId) {
        return postUserTaggsRepository.findByPostId(postId);  // Використовуєте репозиторій для отримання тагів для поста
    }


    public List<User> getTaggedUsersForPost(Long postId) {

        List<PostUserTagg> postUserTaggs = postUserTaggsRepository.findByPostId(postId);


        return postUserTaggs.stream()
                .map(tag -> tag.getUser())
                .collect(Collectors.toList());
    }

    public List<Post> findAllByUserPostedIdInCurrentPage(Long userPagePostedId, Long userPostedId) {
        return postRepository.findAllByUserPostedIdAndUserPagePostedIdOrderByDatePostedDesc(userPagePostedId, userPostedId);
    }


    public List<Post> getAllWithoutPostsOwnerPage(User user, Long id) {
        return postRepository.findByUserPostedNotAndUserPagePostedIdOrderByDatePostedDesc(user, id);
    }

    public List<Post> getAllPosts(Long id) {
        return postRepository.findAllByUserPagePostedIdOrderByDatePostedDesc(id);
    }

    public List<Post> getAllPostsByUserPostedId(Long id) {
        return postRepository.findAllByUserPostedId(id);
    }
}
