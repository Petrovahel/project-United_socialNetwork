package com.united.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class PostUserTaggId implements Serializable {

    private Long postId;
    private Long userId;

    // Конструктори, геттери, сеттери, equals(), hashCode()
    public PostUserTaggId() {
    }

    public PostUserTaggId(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostUserTaggId that = (PostUserTaggId) o;
        return Objects.equals(postId, that.postId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}
