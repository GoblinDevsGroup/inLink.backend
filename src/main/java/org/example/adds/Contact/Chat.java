package org.example.adds.Contact;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
