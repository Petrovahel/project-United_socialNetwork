package com.united.repository;

import com.united.entity.Message;
import com.united.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> getMessageById(Long id);


    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN Message m ON (m.userFrom = u OR m.userTo = u) " +
            "WHERE :userId IN (m.userFrom.id, m.userTo.id) and m.dateDeleted is null")
    List<User> getChatsParticipantsOrdered(@Param("userId") Long userId);


    @Query("select m from Message m where " +
            "((m.userFrom.id = :userId and m.userTo.id = :friendId) " +
            "or (m.userFrom.id = :friendId and m.userTo.id = :userId)) " +
            "and m.dateDeleted is null " +
            "order by m.dateSent desc")
    Page<Message> findMessagesByUserIdsWithPagination(@Param("userId") Long userId,
                                                      @Param("friendId") Long friendId,
                                                      Pageable pageable);


    @Query("select m from Message m where " +
            "((m.userFrom.id = :userId and m.userTo.id = :friendId) " +
            "or (m.userFrom.id = :friendId and m.userTo.id = :userId)) " +
            "and m.dateDeleted is null ")
    List<Message> getAllMessagesByIds(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Modifying
    @Query("update Message m set m.dateRead = current_timestamp where m.dateRead is null and m.userFrom.id = :friendId and m.userTo.id = :userId")
    void markMessagesAsRead(@Param("friendId") Long friendId, @Param("userId") Long userId);


}
