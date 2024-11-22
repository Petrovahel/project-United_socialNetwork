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
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Message text cannot be empty")
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_sent", nullable = false)
    private Date dateSent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_read")
    private Date dateRead;

    @ManyToOne
    @JoinColumn(name = "user_from", nullable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "user_to", nullable = false)
    private User userTo;

    public boolean isRead() {
        return dateRead != null;
    }
}

