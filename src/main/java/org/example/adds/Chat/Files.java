package org.example.adds.Chat;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Table
@Entity
@Data
public class Files implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String name;

    @Column
    private String type;

    @Lob
    @Column
    private byte[] data;

    @Column
    private LocalDateTime uploadedAt;

    public Files(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.uploadedAt = LocalDateTime.now();
    }

    public Files() {
    }
}
