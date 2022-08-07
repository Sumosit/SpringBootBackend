package com.example.pegieback.repository;

import com.example.pegieback.models.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessagesRepository extends JpaRepository<ChatMessages, Long> {
    ChatMessages findChatMessagesByChatName(String chatName);
}
