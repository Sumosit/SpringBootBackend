package com.example.pegieback.controller;

import com.example.pegieback.message.SendChatMessage;
import com.example.pegieback.models.ChatMessage;
import com.example.pegieback.models.ChatMessages;
import com.example.pegieback.models.Role;
import com.example.pegieback.models.User;
import com.example.pegieback.repository.ChatMessageRepository;
import com.example.pegieback.repository.ChatMessagesRepository;
import com.example.pegieback.repository.RoleRepository;
import com.example.pegieback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatMessagesRepository chatMessagesRepository;

    @MessageMapping("/chat/{id1}/{id2}")
    @SendTo("/topic/chat/{id1}/{id2}")
    public ChatMessage greeting(SendChatMessage message, @DestinationVariable Long id1, @DestinationVariable String id2) throws Exception {
        Thread.sleep(1000);
        System.out.println(1);
        SendChatMessage sendChatMessage = new SendChatMessage();
//        System.out.println(sendChatMessage.getImgIds());
        User user;
        if (userRepository.findUserById(id1) == null) {
            System.out.println(11);
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findRoleByName("ROLE_USER"));
            user = new User(id1, "Bot" + id1, roles);
            System.out.println(112);
            user = userRepository.save(user);
            System.out.println(113);
        } else {
            System.out.println(114);
            user = userRepository.findUserById(id1);
            System.out.println(115);
        }
        System.out.println(3);

        ChatMessages chatMessages;
        if (chatMessagesRepository.findByChatName(id1 + id2) != null) {
            System.out.println(33);
            chatMessages = chatMessagesRepository.findByChatName(id1 + id2);
        } else {
            System.out.println(333);
            chatMessages = chatMessagesRepository.findByChatName(id2 + id1);
        }
        System.out.println(user.getId());
        System.out.println(3333);
        ChatMessage chatMessage = chatMessageRepository.save(new ChatMessage(null, user, message.getMessage(), message.getImgIds()));

        System.out.println(33333);
        if (chatMessages == null) {
            System.out.println(4);
            List<ChatMessage> chatMessageList = new ArrayList<>();
            chatMessageList.add(chatMessage);
            chatMessages = new ChatMessages(null, id1 + id2, chatMessageList);
        } else {
            System.out.println(5);
            List<ChatMessage> chatMessageList = chatMessages.getChatMessageList();
            chatMessageList.add(chatMessage);
            chatMessages.setChatMessageList(chatMessageList);
        }

        chatMessagesRepository.save(chatMessages);
        return chatMessage;
    }

    @GetMapping("/api/chat/messages/{id1}/{id2}")
    public List<ChatMessage> chatListMessages(@PathVariable Long id1, @PathVariable String id2) {
        User user = userRepository.findUserById(id1);
        if (user == null) {
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(null, "ROLE_USER"));
            user = new User(id1, "Bot", roles);
            userRepository.save(user);
            user.setUsername("Bot " + user.getUsername());
            userRepository.save(user);
        }

        ChatMessages chatMessages;
        if (chatMessagesRepository.findByChatName(id1 + id2) != null) {
            chatMessages = chatMessagesRepository.findByChatName(id1 + id2);
        } else {
            chatMessages = chatMessagesRepository.findByChatName(id2 + id1);
        }

        if (chatMessages == null) {
            List<ChatMessage> chatMessageList = new ArrayList<>();
            chatMessages = new ChatMessages(null, id1 + id2, chatMessageList);
        } else {
            List<ChatMessage> chatMessageList = chatMessages.getChatMessageList();
            chatMessages.setChatMessageList(chatMessageList);
        }

        chatMessagesRepository.save(chatMessages);
        if (chatMessagesRepository.findByChatName(id1 + id2) != null) {
            return chatMessagesRepository.findByChatName(id1 + id2).getChatMessageList();
        } else {
            return chatMessagesRepository.findByChatName(id2 + id1).getChatMessageList();
        }
    }


}
