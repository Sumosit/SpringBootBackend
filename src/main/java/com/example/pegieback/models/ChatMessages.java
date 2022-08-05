package com.example.pegieback.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_message", schema = "public")
public class ChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatName;

    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<ChatMessage> chatMessageList;

}
