package com.united.handler;

import com.united.entity.Friendship;
import com.united.exception.FriendshipValidationException;
import com.united.repository.FriendshipRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

@AllArgsConstructor
public class ValidationMemoryForNewFriendOrNewRequest extends Validation {
    private final int maxNumberForFriends;
    private final int maxNumberForSentRequest;
    private final FriendshipRepository friendshipRepository;


    @SneakyThrows
    @Override
    public boolean check(Long userId, Long friendId) {
        List<Friendship> friendshipsForUser = friendshipRepository.findAllFriendsByUserId(userId);
        List<Friendship> requestsSentForUser = friendshipRepository.findAllSentRequestByUserId(userId);

        List<Friendship> friendshipsForFriend = friendshipRepository.findAllFriendsByUserId(friendId);
        List<Friendship> requestsSentForFriend = friendshipRepository.findAllSentRequestByUserId(friendId);

        if (requestsSentForUser.size() <= maxNumberForSentRequest && friendshipsForUser.size() <= maxNumberForFriends
                && requestsSentForFriend.size() <= maxNumberForSentRequest && friendshipsForFriend.size() <= maxNumberForFriends) {
            return checkNext(userId, friendId);
        }
        throw new FriendshipValidationException("Do not free space for new friend");
    }

}
