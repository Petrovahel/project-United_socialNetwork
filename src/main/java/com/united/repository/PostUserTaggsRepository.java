package com.united.repository;

import com.united.entity.PostUserTagg;
import com.united.entity.PostUserTaggId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostUserTaggsRepository extends JpaRepository<PostUserTagg, PostUserTaggId> {
    List<PostUserTagg> findByPostId(Long postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PostUserTagg p WHERE p.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);


}
