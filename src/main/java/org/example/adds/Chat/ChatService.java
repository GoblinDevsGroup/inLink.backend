package org.example.adds.Chat;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.adds.Chat.Dto.ChatResponse;
import org.example.adds.Files.FileRepo;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepo chatRepo;
    private final UsersRepo usersRepo;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final FileRepo fileRepo;

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
                            chat.getFile().getId(),
                            chat.getCreatedAt()
                    ))
                    .toList();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Database constraint violation occurred", e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching chat messages", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

//    public void send(String senderPhone, ChatRequest request) {
//        Users sender = usersRepo.findByPhone(senderPhone)
//                .orElseThrow(() -> new NoSuchElementException("User not found"));
//
//        Users receiver = (request.receiverId() == null)
//                ? getAdmin()
//                : usersRepo.findById(request.receiverId())
//                .orElseThrow(() -> new NoSuchElementException("Receiver not found"));
//
//        ChatResponse response = saveMessage(new Chat(sender, receiver, request.message()));
//
//        simpMessagingTemplate.convertAndSendToUser(
//                receiver.getId().toString(),
//                "/queue/private",
//                response);
//
//        simpMessagingTemplate.convertAndSendToUser(
//                sender.getId().toString(),
//                "/queue/private",
//                response);
//    }

    @SneakyThrows
    @Transactional
    public void send(String senderPhone, UUID receiverId, String message, MultipartFile file) {
        Users sender = usersRepo.findByPhone(senderPhone)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Users receiver = (receiverId == null)
                ? getAdmin()
                : usersRepo.findById(receiverId)
                .orElseThrow(() -> new NoSuchElementException("Receiver not found"));

        Files entity = (file != null && !file.isEmpty())
                ? fileRepo.save(new Files(file.getOriginalFilename(), file.getContentType(), file.getBytes()))
                : null;

        Chat chat = (message != null)
                ? new Chat(sender, receiver, message, entity)
                : new Chat(sender, receiver, entity);

        ChatResponse response = saveMessage(chat);

        sendToUser(sender, response);
        sendToUser(receiver, response);
    }


    private void sendToUser(Users user, ChatResponse response) {
        simpMessagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/queue/private",
                response);
    }

    private ChatResponse saveMessage(Chat chat) {
        Chat savedChat = chatRepo.save(chat);

        return new ChatResponse(
                savedChat.getId(),
                savedChat.getSender().getUserRole(),
                savedChat.getMessage() != null ? savedChat.getMessage() : null,
                savedChat.getFile().getId() != null ? savedChat.getFile().getId() : null,
                savedChat.getCreatedAt()
        );
    }

    public void editMessage(UUID messageId, String message) {
        Chat chat = chatRepo.findById(messageId)
                .orElseThrow(()->new NoSuchElementException("message not found"));
        chat.setMessage(message);
        chatRepo.save(chat);
    }
}
