package com.united.entity;

import jakarta.persistence.*;
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
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_from_id", nullable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "user_to_id", nullable = false)
    private User userTo;

    @Enumerated(EnumType.ORDINAL)
    private FriendshipStatus status;

    @Column(name = "friendship_accept_at", updatable = false)
    private Date friendshipAcceptAt;
}
