package com.united.config;

import com.united.handler.Validation;
import com.united.handler.ValidationForExistenceRelationship;
import com.united.handler.ValidationMemoryForNewFriendOrNewRequest;
import com.united.repository.FriendshipRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

    @Bean
    public ValidationForExistenceRelationship validationForExistenceRelationship(FriendshipRepository friendshipRepository) {
        return new ValidationForExistenceRelationship(friendshipRepository);
    }

    @Bean
    public ValidationMemoryForNewFriendOrNewRequest validationMemoryForNewFriendOrNewRequest(FriendshipRepository friendshipRepository) {
        return new ValidationMemoryForNewFriendOrNewRequest(90, 10, friendshipRepository);
    }

    @Bean
    public Validation validationAddition(ValidationForExistenceRelationship validationForExistenceRelationship, ValidationMemoryForNewFriendOrNewRequest validationMemoryForNewFriendOrNewRequest) {
        return Validation.link(
                validationForExistenceRelationship,
                validationMemoryForNewFriendOrNewRequest
        );
    }
}
