package org.example.adds.Contact;

import lombok.AllArgsConstructor;
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

    private UUID adminId() {
        return usersRepo.findByPhone("+998900000000").get().getId();
    }

    public Chat saveMessageFromUser(ChatMessage request) {

        if (chatRepo.existsByUserId(request.getUserId())) {
            String chatName = chatRepo.findByUserId(request.getUserId()).getChatName();
            Chat chat = new Chat();
            chat.setChatName(chatName);
            chat.setUserId(request.getUserId());
            chat.setAdminId(adminId()); //admin
            chat.setMessage(request.getContent());
            chat.setTime(LocalDateTime.now());
            return chatRepo.save(chat);
        }
        else
        {
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
        return String.format("%06d", new Random().nextInt(1000000)); // Always generates a 4-digit number
    }

    public Chat saveMessageFromAdmin(ChatMessageFromAdmin message) {

        UUID userId = chatRepo.findByChatName(message.chatName())
                .getLast().getUserId();

        if (chatRepo.existsByChatName(message.chatName())) {
            Chat chat = new Chat();
            chat.setChatName(message.chatName());
            chat.setUserId(userId);
            chat.setAdminId(message.adminId()); //admin
            chat.setMessage(message.content());
            chat.setTime(LocalDateTime.now());
            return chatRepo.save(chat);
        }
        else
        {
            return openNewChat(message, userId, adminId());
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

    public ChatResponse viewOneChat(String chatName) {

        List<Chat> chat = chatRepo.findByChatName(chatName);

        if (chat.isEmpty()){
            throw new NoSuchElementException("Data not found");
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
        response.setChatName(chatName);
        response.setMessages(messages);

        return response;
    }
}
