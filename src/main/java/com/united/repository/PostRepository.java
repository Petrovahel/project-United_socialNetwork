package com.united.repository;

import com.united.entity.Post;
import com.united.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostById(Long id);

    List<Post> findAllByUserPostedIdAndUserPagePostedIdOrderByDatePostedDesc(Long userPagePostedId, Long userPostedId);

    List<Post> findAllByUserPagePostedIdOrderByDatePostedDesc(Long userId);

    List<Post> findByUserPostedNotAndUserPagePostedIdOrderByDatePostedDesc(User user, Long pageUserId);

    List<Post> findAllByUserPostedId(Long id);

}
