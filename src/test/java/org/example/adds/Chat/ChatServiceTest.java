package org.example.adds.Chat;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private ChatRepo chatRepo;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private ChatService chatService;

    private Users sender;
    private Users receiver;
    private UUID receiverId;
    private MultipartFile file;

    @BeforeEach
    void setUp() throws IOException {
        sender = new Users();
        sender.setId(UUID.randomUUID());
        sender.setPhone("1234567890");

        receiver = new Users();
        receiverId = UUID.randomUUID();
        receiver.setId(receiverId);

        file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("file.txt");
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getBytes()).thenReturn("File content".getBytes());

        when(usersRepo.findByPhone(sender.getPhone())).thenReturn(Optional.of(sender));
        when(usersRepo.findById(receiverId)).thenReturn(Optional.of(receiver));
    }

    @Test
    void shouldSendOnlyMessage() {
        String message = "Hello";

        chatService.send(sender.getPhone(), receiverId, message, null);

        verify(chatRepo, times(1)).save(any(Chat.class));
        verify(simpMessagingTemplate, times(2)).convertAndSendToUser(anyString(), eq("/queue/private"), any());
    }

    @Test
    void shouldSendOnlyFile() throws Exception {
        chatService.send(sender.getPhone(), receiverId, null, file);

        verify(chatRepo, times(1)).save(any(Chat.class));
        verify(simpMessagingTemplate, times(2)).convertAndSendToUser(anyString(), eq("/queue/private"), any());
    }

    @Test
    void shouldSendMessageAndFile() throws Exception {
        String message = "Hello with file";

        chatService.send(sender.getPhone(), receiverId, message, file);

        verify(chatRepo, times(1)).save(any(Chat.class));
        verify(simpMessagingTemplate, times(2)).convertAndSendToUser(anyString(), eq("/queue/private"), any());
    }

    @Test
    void shouldSendMessageToAdminIfReceiverIdIsNull() {
        when(chatService.getAdmin()).thenReturn(receiver);

        String message = "Message to admin";
        chatService.send(sender.getPhone(), null, message, null);

        verify(chatRepo, times(1)).save(any(Chat.class));
        verify(simpMessagingTemplate, times(2)).convertAndSendToUser(anyString(), eq("/queue/private"), any());
    }

    @Test
    void shouldThrowExceptionWhenSenderNotFound() {
        when(usersRepo.findByPhone("wrongNumber")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            chatService.send("wrongNumber", receiverId, "Test", null);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenReceiverNotFound() {
        when(usersRepo.findById(UUID.randomUUID())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            chatService.send(sender.getPhone(), UUID.randomUUID(), "Test", null);
        });

        assertEquals("Receiver not found", exception.getMessage());
    }
}
