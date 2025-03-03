package org.example.adds.Chat;

import lombok.AllArgsConstructor;
import org.example.adds.Chat.Dto.ChatRequest;
import org.example.adds.Chat.Dto.ChatResponse;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepo chatRepo;
    private final UsersRepo usersRepo;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public Users getAdmin() {
        return usersRepo.findByUserRole(Users.UserRole.ROLE_ADMIN);
    }

    public List<ChatResponse> viewOneChat(UUID userId) {
        try {
            List<Chat> chats = chatRepo.findChatsByUserId(userId);
            return chats.stream()
                    .map(chat -> new ChatResponse(
                            chat.getId(),
                            chat.getSender().getUserRole(),
                            chat.getMessage(),
                            chat.getTime()
                    ))
                    .toList();
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Database constraint violation occurred", e);
        }catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching chat messages", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    public void send(String senderPhone, ChatRequest request) {
        Users sender = usersRepo.findByPhone(senderPhone)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Users receiver = (request.receiverId() == null)
                ? getAdmin()
                : usersRepo.findById(request.receiverId())
                .orElseThrow(() -> new NoSuchElementException("Receiver not found"));

        ChatResponse response = saveMessage(new Chat(sender, receiver, request.message()));

        simpMessagingTemplate.convertAndSendToUser(
                receiver.getId().toString(),
                "/queue/private",
                response);

        simpMessagingTemplate.convertAndSendToUser(
                sender.getId().toString(),
                "/queue/private",
                response);
    }

    private ChatResponse saveMessage(Chat chat) {
        Chat chat1 = chatRepo.save(chat);
        return new ChatResponse(
                chat1.getId(),
                chat1.getSender().getUserRole(),
                chat1.getMessage(),
                chat1.getTime()
        );
    }
}
