package com.united.handler;

import com.united.entity.Friendship;
import com.united.entity.FriendshipStatus;
import com.united.repository.FriendshipRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidationForExistenceRelationship extends Validation {
    private final FriendshipRepository friendshipRepository;

    @Override
    public boolean check(Long userId, Long friendId) {
        Friendship friendship = friendshipRepository.getFriendshipByIds(userId, friendId);
        if (friendship != null && friendship.getStatus().equals(FriendshipStatus.REQUEST_SENT)) {
            return false;
        } else {
            return checkNext(userId, friendId);
        }
    }
}
