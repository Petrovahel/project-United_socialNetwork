package com.united.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Size(max = 200, message = "Message must be less than 200 characters")
    @Pattern(regexp = "^(?!.*https?://).*", message = "Message cannot contain links")
    private String message;

    @Column(name = "date_posted", nullable = false, updatable = false)
    private Date datePosted;

    private String location;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostUserTagg> postUserTaggs = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "user_posted_id", nullable = false)
    private User userPosted;

    @ManyToOne
    @JoinColumn(name = "user_page_posted_id", nullable = false)
    private User userPagePosted;

    //TODO
    //levels permission

    //TODO
    //comments


    @PrePersist
    protected void onCreate() {
        this.datePosted = new Date();
    }
}
