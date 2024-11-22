package com.findme.controller;

import com.findme.entity.Message;
import com.findme.entity.User;
import com.findme.service.MessageService;
import com.findme.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/messages")
    public String findAll(Model model){
        List<Message> messages = messageService.findAll();
        model.addAttribute("messages", messages);
        return "messages";

    }

    public Message getMessageById(Long id){
        return messageService.getMessageById(id);
    }

    public Message saveMessage(Message message){
        return messageService.saveMessage(message);
    }

    public void deleteMessage(Long id){
        messageService.deleteMessage(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Long id, @RequestBody Message updatedMessage) {
        Message message = messageService.updateMessage(id, updatedMessage);
        return ResponseEntity.ok(message);
    }
}
