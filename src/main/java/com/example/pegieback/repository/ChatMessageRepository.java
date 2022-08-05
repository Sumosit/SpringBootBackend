package com.example.pegieback.repository;

import com.example.pegieback.models.ChatMessage;
import com.example.pegieback.models.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
