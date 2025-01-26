package com.united.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "post_user_tagg")
public class PostUserTagg {

    @EmbeddedId
    private PostUserTaggId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    public PostUserTagg(Post post, User user) {
        this.post = post;
        this.user = user;
        this.id = new PostUserTaggId(post.getId(), user.getId());
    }
}
