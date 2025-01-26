package com.united.repository;

import com.united.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("select f from Friendship f where (f.userFrom.id = :userId and f.userTo.id = :friendId) " +
            "or (f.userFrom.id = :friendId and f.userTo.id = :userId)")
    Friendship getFriendshipByIds(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query("select f from Friendship f where (f.userFrom.id = :id or f.userTo.id = :id) and f.status = 0")
    List<Friendship> findAllFriendsByUserId(@Param("id") Long id);

    @Modifying
    @Query(value = "delete from friendship f where (f.user_from_id = :userId and f.user_to_id = :friendId) " +
            "or (f.user_from_id = :friendId and f.user_to_id = :userId)", nativeQuery = true)
    void deleteByUserIds(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query("select f from Friendship f where (f.userTo.id = :id) and f.status = 2")
    List<Friendship> findAllFriendsRequestByUserId(@Param("id") Long id);

    @Query("select f from Friendship f where (f.userFrom.id = :id) and f.status = 2")
    List<Friendship> findAllSentRequestByUserId(@Param("id") Long id);

    @Modifying
    @Query("update Friendship f set f.status = 0, f.friendshipAcceptAt = CURRENT_TIMESTAMP WHERE (f.userFrom.id = :userId AND f.userTo.id = :friendId) OR (f.userFrom.id = :friendId AND f.userTo.id = :userId)")
    void acceptFriendRequest(@Param("userId") Long userId, @Param("friendId") Long friendId);
}



