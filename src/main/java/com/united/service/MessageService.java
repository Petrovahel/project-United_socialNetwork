package com.united.service;

import com.united.entity.Message;
import com.united.entity.User;
import com.united.exception.handler.NotFoundException;
import com.united.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;


    public Message getMessageById(Long id) {
        return messageRepository.getMessageById(id).orElseThrow(() -> new NotFoundException("Message with id " + id + " not found"));
    }


    public Page<Message> getMessagesByIdsWithPagination(Long userId, Long friendId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("dateSent"))); // Сортуємо за датою на зменшення
        return messageRepository.findMessagesByUserIdsWithPagination(userId, friendId, pageable);
    }


    public List<Message> getAllMessagesByIds(Long userId, Long friendId) {
        return messageRepository.getAllMessagesByIds(userId, friendId);
    }


    public List<User> getChatsParticipants(Long userId) {
        List<User> users = new ArrayList<>();
        for (User user : messageRepository.getChatsParticipantsOrdered(userId)) {
            if (!user.getId().equals(userId)) {
                users.add(user);
            }
        }
        return users;
    }

    public void markMessagesAsRead(List<Message> messages, User friend) {
        for (Message message : messages) {
            if (message.getDateRead() == null && message.getUserFrom().equals(friend)) {
                message.setDateRead(new Date());
            }
        }
        messageRepository.saveAll(messages);
    }

    @Transactional
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Long messageId, Message updatedMessage) {
        Optional<Message> existingMessageOpt = messageRepository.findById(messageId);

        if (existingMessageOpt.isPresent()) {
            Message existingMessage = existingMessageOpt.get();

            existingMessage.setText(updatedMessage.getText());
            existingMessage.setDateSent(new Date());
            existingMessage.setDateRead(new Date());
            existingMessage.setUserFrom(updatedMessage.getUserFrom());
            existingMessage.setUserTo(updatedMessage.getUserTo());


            messageRepository.save(existingMessage);
        } else {
            throw new IllegalArgumentException("Message with id " + messageId + " does not exist.");
        }

        return updatedMessage;
    }

    public List<Message> getMessagesByIds(List<Long> ids) {
        return messageRepository.findAllById(ids);
    }


}
