package org.example.adds.Contact;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private String chatName;
    private List<MessageResponse> messages;
}
