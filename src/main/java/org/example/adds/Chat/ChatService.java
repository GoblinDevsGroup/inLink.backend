package org.example.adds.Chat;

import lombok.AllArgsConstructor;
import org.example.adds.Chat.Dto.ChatMessage;
import org.example.adds.Chat.Dto.ChatMessageFromAdmin;
import org.example.adds.Chat.Dto.ChatResponse;
import org.example.adds.Chat.Dto.MessageResponse;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepo chatRepo;
    private final UsersRepo usersRepo;

    public UUID adminId() {
        return usersRepo.findByUserRole(Users.UserRole.ROLE_ADMIN).getId();
    }

    public Chat saveMessageFromUser(ChatMessage request) {

        if (chatRepo.existsByUserId(request.getUserId())) {
            String chatName = chatRepo.findByUserId(request.getUserId()).getLast().getChatName();
            Chat chat = new Chat();
            chat.setChatName(chatName);
            chat.setUserId(request.getUserId());
            chat.setAdminId(adminId()); //admin
            chat.setMessage(request.getContent());
            chat.setTime(LocalDateTime.now());

            return chatRepo.save(chat);
        }
        else
        {                                 //to,         //from
            return openNewChat(request, adminId(), request.getUserId());
        }

    }

    private Chat openNewChat(ChatMessage request, UUID to, UUID from) {
        Chat chat = new Chat();
        String chatName = generateRandomName();
        chat.setChatName(chatName);
        chat.setUserId(from);
        chat.setMessage(request.getContent());
        chat.setAdminId(to);
        chat.setTime(LocalDateTime.now());
        return chatRepo.save(chat);
    }

    private Chat openNewChat(ChatMessageFromAdmin request, UUID to, UUID from) {
        Chat chat = new Chat();
        String chatName = generateRandomName();
        chat.setChatName(chatName);
        chat.setUserId(from);
        chat.setMessage(request.content());
        chat.setAdminId(to);
        chat.setTime(LocalDateTime.now());
        return chatRepo.save(chat);
    }

    private String generateRandomName() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public Chat saveMessageFromAdmin(ChatMessageFromAdmin message) {
        if (chatRepo.existsByUserId(message.userId())) {
            Chat chat = new Chat();
            Chat chat1 = chatRepo.findByUserId(message.userId()).getLast();
            chat.setChatName(chat1.getChatName());
            chat.setUserId(message.userId());
            chat.setAdminId(message.adminId()); //admin
            chat.setMessage(message.content());
            chat.setTime(LocalDateTime.now());
            return chatRepo.save(chat);
        }
        else
        {                                 //to              //from
            return openNewChat(message, message.userId(), adminId());
        }
    }

    public List<ChatResponse> findAllByGroup() {
        List<Chat> chats = chatRepo.findAll();

        if (chats.isEmpty()){
            throw new NoSuchElementException("Data not found");
        }
        Map<String, List<Chat>> groupedByChatName = chats.stream()
                .collect(Collectors.groupingBy(Chat::getChatName));

        return groupedByChatName.entrySet().stream()
                .map(entry -> {
                    String chatName = entry.getKey();
                    List<MessageResponse> messages = entry.getValue().stream()
                            .map(chat -> new MessageResponse(
                                    chat.getAdminId(),
                                    chat.getUserId(),
                                    chat.getMessage(),
                                    chat.getTime()
                            ))
                            .collect(Collectors.toList());

                    ChatResponse chatResponse = new ChatResponse();
                    chatResponse.setChatName(chatName);
                    chatResponse.setMessages(messages);

                    return chatResponse;
                })
                .collect(Collectors.toList());
    }

    public ChatResponse viewOneChat(UUID userId) {

        List<Chat> chat = chatRepo.findByUserId(userId);

        if (chat.isEmpty()) {
            return new ChatResponse();
        }

        List<MessageResponse> messages = chat.stream().map(
                chat1 -> new MessageResponse(
                        chat1.getAdminId(),
                        chat1.getUserId(),
                        chat1.getMessage(),
                        chat1.getTime()
                )
        ).collect(Collectors.toList());

        ChatResponse response = new ChatResponse();
        response.setChatName(chat.get(chat.size()-1).getChatName());
        response.setMessages(messages);

        return response;
    }
}
