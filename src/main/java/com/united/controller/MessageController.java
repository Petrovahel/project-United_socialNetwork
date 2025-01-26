package com.united.controller;

import com.united.entity.Message;
import com.united.entity.MessageEditDTO;
import com.united.entity.User;
import com.united.service.MessageService;
import com.united.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/chats")
    public String showAllChats(Authentication authentication, Model model) {
        User authUser = userService.getUserByNickname(authentication.getName());

        List<User> corespondents = messageService.getChatsParticipants(authUser.getId());

        model.addAttribute("user", authUser);
        model.addAttribute("corespondents", corespondents);
        return "chats";
    }

    @GetMapping("/chatPage/{nickname}")
    public String showChat(
            @PathVariable String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            Authentication authentication,
            Model model) {

        User friend = userService.getUserByNickname(nickname);
        User authUser = userService.getUserByNickname(authentication.getName());

        Page<Message> messagePage = messageService.getMessagesByIdsWithPagination(
                authUser.getId(),
                friend.getId(),
                page,
                pageSize
        );

        messageService.markMessagesAsRead(messagePage.getContent(), friend);

        model.addAttribute("messages", messagePage.getContent());
        model.addAttribute("hasMoreMessages", messagePage.hasNext());
        model.addAttribute("currentPage", page);
        model.addAttribute("authUser", authUser);
        model.addAttribute("friend", friend);

        return "chatPage";
    }


    @DeleteMapping("/deleteMessages")
    public ResponseEntity<String> deleteMessages(@RequestBody List<Long> messageIds, Authentication authentication) {
        User authUser = userService.getUserByNickname(authentication.getName());

        List<Message> messages = messageService.getMessagesByIds(messageIds);

        boolean allOwnedByUser = messages.stream()
                .allMatch(message -> message.getUserFrom().getId().equals(authUser.getId()));

        if (!allOwnedByUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own messages.");
        }


        for (Message message : messages) {
            message.setDateDeleted(new Date());
            messageService.saveMessage(message);
        }
        return ResponseEntity.ok("Messages deleted successfully.");
    }


    @PostMapping("/saveMessage/{nickname}")
    public String saveMessage(@PathVariable String nickname,
                              @RequestParam Long userFrom,
                              @RequestParam Long userTo,
                              @RequestParam String message) {

        User authUser = userService.getUserById(userFrom);
        User friend = userService.getUserById(userTo);

        if (message != null && !message.isEmpty()) {
            Message newMessage = new Message();
            newMessage.setUserFrom(authUser);
            newMessage.setUserTo(friend);
            newMessage.setText(message);
            newMessage.setDateSent(new Date());
            newMessage.setDateEdited(null);
            newMessage.setDateDeleted(null);
            newMessage.setDateRead(null);


            messageService.saveMessage(newMessage);

        }
        return "redirect:/chatPage/" + nickname;
    }

    @DeleteMapping("/deleteMessage/{messageId}")
    @ResponseBody
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId, Authentication authentication) {
        Message message = messageService.getMessageById(messageId);

        if (!message.getUserFrom().getNickname().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        message.setDateDeleted(new Date());
        messageService.saveMessage(message);
        log.info("Message deleted: " + message);
        return ResponseEntity.ok("Message deleted successfully.");
    }

    @DeleteMapping("/deleteChat/{nickname}")
    @ResponseBody
    public ResponseEntity<String> deleteChat(@PathVariable String nickname, Authentication authentication) {
        User authUser = userService.getUserByNickname(authentication.getName());
        User friend = userService.getUserByNickname(nickname);

        log.info("Got users: " + authUser + ", " + friend);

        List<Message> messages = messageService.getAllMessagesByIds(authUser.getId(), friend.getId());
        log.info("Got messages: " + messages);


        for (Message message : messages) {
            if (message.getDateDeleted() == null) {
                message.setDateDeleted(new Date());
                messageService.saveMessage(message);
            }
        }

        return ResponseEntity.ok("Chat deleted successfully.");
    }

    @PutMapping(value = "/editMessage/{messageId}")
    public ResponseEntity<String> editMessage(@PathVariable Long messageId, @RequestBody MessageEditDTO messageEditDTO) {
        String newText = messageEditDTO.getText();
        Message message = messageService.getMessageById(messageId);
        message.setText(newText);
        message.setDateEdited(new Date());
        messageService.saveMessage(message);
        return ResponseEntity.ok("Message edited successfully.");
    }
}
