package org.example.adds.Chat;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column
    private String chatName;

    @Column
    private UUID userId;

    @Column
    private UUID adminId;

    @Column
    private String message;

    @Column
    private LocalDateTime time;
}
