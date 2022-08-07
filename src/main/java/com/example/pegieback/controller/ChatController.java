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
@CrossOrigin(origins = "*")
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
    public ChatMessage greeting(SendChatMessage message, @DestinationVariable String id1, @DestinationVariable String id2) throws Exception {
        Thread.sleep(1000);
        if (!userRepository.existsById(Long.valueOf(id1))) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findRoleByName("ROLE_USER"));
            userRepository.save(new User(null, "bot", roles));
            userRepository.save(new User(null, "bot", roles));
            userRepository.save(new User(null, "bot", roles));
            userRepository.save(new User(null, "bot", roles));
            userRepository.save(new User(null, "bot", roles));
            userRepository.save(new User(null, "bot", roles));
            userRepository.save(new User(null, "bot", roles));
        }

        User user = userRepository.findUserById(Long.valueOf(id1));
        ChatMessage chatMessage = chatMessageRepository.save(new ChatMessage(null, user, message.getMessage(), message.getImgIds()));
        ChatMessages chatMessages;
        List<ChatMessage> chatMessageList = new ArrayList<>();

        if (chatMessagesRepository.findChatMessagesByChatName(id1 + id2) == null) {
            chatMessageList.add(chatMessage);
            chatMessagesRepository.save(new ChatMessages(null, id1 + id2, chatMessageList));
        } else {
            chatMessages = chatMessagesRepository.findChatMessagesByChatName(id1 + id2);
            chatMessages.getChatMessageList().add(chatMessage);
            chatMessagesRepository.save(chatMessages);
        }
        return chatMessage;
    }

    @GetMapping("/api/chat/messages/{id1}/{id2}")
    public List<ChatMessage> chatListMessages(@PathVariable String id1, @PathVariable String id2) {
        return chatMessagesRepository.findChatMessagesByChatName(id1 + id2).getChatMessageList();
    }

}
