//package org.example.adds.Contact;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//public class MessageService {
//
//    private final MessageRepo messageRepo;
//
//    public Message saveMessage(ChatMessage chatMessage) {
//        Message message = new Message();
//        message.setUserId(chatMessage.getUserId());
//        message.setContent(chatMessage.getContent());
//        message.setSentAt(LocalDateTime.now());
//        return messageRepo.save(message);
//    }
//
//    public List<Message> getAll() {
//        return messageRepo.findAll();
//    }
//}
