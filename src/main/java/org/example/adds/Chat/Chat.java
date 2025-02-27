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

    @Column
    private String message;

    @Lob
    @Column
    private byte[] file;

    @Column
    private LocalDateTime time;

    public Chat(Users sender,
                Users receiver,
                String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = LocalDateTime.now();
    }

    public Chat() {}
}
