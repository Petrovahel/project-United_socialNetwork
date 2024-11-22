package com.findme.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Invalid phone number format")
    private String phone;

    private String country;
    private String city;

    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 150, message = "Age cannot exceed 150")
    private Integer age;

    @CreationTimestamp
    @Column(name = "date_registered", updatable = false)
    private Date dateRegistered;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_last_active")
    private Date dateLastActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationShipStatus relationShip;

    private String religion;
    private String school;
    private String university;

    @OneToMany(mappedBy = "userFrom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "userTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messagesReceived;

    private String password;


    // Інтереси користувача, зберігаються як окрема таблиця
    // @ElementCollection
    // @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    // @Column(name = "interest")
    // private List<String> interests;

}
