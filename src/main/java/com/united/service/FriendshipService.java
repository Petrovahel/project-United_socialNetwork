package com.united.service;

import com.united.entity.Friendship;
import com.united.entity.FriendshipStatus;
import com.united.entity.User;
import com.united.exception.RemoveException;
import com.united.repository.FriendshipRepository;
import com.united.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;


    public Friendship getFriendshipByIds(Long userId, Long friendId) {

        return friendshipRepository.getFriendshipByIds(userId, friendId);

    }

    public List<User> getFriends(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllFriendsByUserId(userId);

        List<Long> friendsId = friendships.stream()
                .map(f -> f.getUserTo().getId().equals(userId) ? f.getUserFrom().getId() : f.getUserTo().getId())
                .toList();

        return userRepository.findAllById(friendsId);
    }

    @Transactional
    public RemoveException removeFriendship(Long userId, Long friendId) {
        Friendship friendship = friendshipRepository.getFriendshipByIds(userId, friendId);
        Date currentDate = new Date();
        if (friendship != null) {
            long differenceInMillis = currentDate.getTime() - friendship.getFriendshipAcceptAt().getTime();
            long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);

            if (friendship.getStatus() == FriendshipStatus.FRIENDS && differenceInDays >= 3) {
                friendshipRepository.deleteByUserIds(userId, friendId);
            } else {
                return new RemoveException("You cannot remove this friend because your friendship lasts less than 3 days.");
            }
        }
        return new RemoveException("You don`t have friendship with this user.");
    }


    @Transactional
    public void removeRequest(Long userId, Long friendId) {
        friendshipRepository.deleteByUserIds(userId, friendId);
    }

    public List<User> getRequestReceived(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllFriendsRequestByUserId(userId);

        List<Long> requestReceived = friendships.stream()
                .filter(f -> f.getUserTo().getId().equals(userId))
                .map(f -> f.getUserFrom().getId())
                .toList();

        return userRepository.findAllById(requestReceived);
    }

    public List<User> getSentRequest(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllSentRequestByUserId(userId);

        List<Long> requestSent = friendships.stream()
                .filter(f -> f.getUserFrom().getId().equals(userId))
                .map(f -> f.getUserTo().getId())
                .toList();

        return userRepository.findAllById(requestSent);
    }


    @Transactional
    public void acceptFriend(Long userId, Long friendId) {
        friendshipRepository.acceptFriendRequest(userId, friendId);
    }

    public void addNewRequest(Friendship friendship) {
        friendshipRepository.save(friendship);
    }


}
