package com.united.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_from", nullable = false)
    private User userFrom;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_to", nullable = false)
    private User userTo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_sent", nullable = false)
    private Date dateSent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_edited")
    private Date dateEdited;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_deleted")
    private Date dateDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_read")
    private Date dateRead;

    @NotEmpty(message = "Message text cannot be empty")
    private String text;

    public boolean isRead() {
        return dateRead != null;
    }
}

