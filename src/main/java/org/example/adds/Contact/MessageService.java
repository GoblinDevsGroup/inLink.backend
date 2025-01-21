package org.example.adds.Contact;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepo messageRepo;

    public Message saveMessage(ChatMessage chatMessage) {
        Message message = new Message();
        message.setSenderId(chatMessage.getSenderId());
        message.setRecipientId(chatMessage.getRecipientId());
        message.setContent(chatMessage.getContent());
        message.setSentAt(LocalDateTime.now());
        return messageRepo.save(message);
    }
}
