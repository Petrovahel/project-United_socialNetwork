package com.findme.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Post message cannot be empty")
    @Column(nullable = false, length = 1000)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_posted", nullable = false, updatable = false)
    private Date datePosted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_posted_id", nullable = false)
    private User userPosted;
    //TODO
    //levels permission

    //TODO
    //comments


    @PrePersist
    protected void onCreate() {
        this.datePosted = new Date();
    }
}
