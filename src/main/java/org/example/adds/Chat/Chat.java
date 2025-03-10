package org.example.adds.Chat;

import jakarta.persistence.*;
import lombok.Data;
import org.example.adds.Users.Users;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class Chat implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Users receiver;

    @JoinColumn(columnDefinition = "TEXT")
    private String message;

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Files file;

    @Column
    private LocalDateTime createdAt;

    @JoinColumn
    private LocalDateTime updatedAt;

    public Chat(Users sender,
                Users receiver,
                String message,
                Files file) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.file = file;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Chat(Users sender,
                Users receiver,
                Files file) {
        this.sender = sender;
        this.receiver = receiver;
        this.file = file;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Chat(Users sender,
                Users receiver,
                String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Chat() {
    }
}
