package org.example.adds.Contact.Dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private String chatName;
    private List<MessageResponse> messages;
}
