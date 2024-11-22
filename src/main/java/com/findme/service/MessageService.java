package com.findme.service;

import com.findme.entity.Message;
import com.findme.entity.User;
import com.findme.repository.MessageRepository;
import com.findme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> findAll(){
        return messageRepository.findAll();
    }

    public Message getMessageById(Long id){
        return messageRepository.findById(id).orElse(null);
    }

    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
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
}
