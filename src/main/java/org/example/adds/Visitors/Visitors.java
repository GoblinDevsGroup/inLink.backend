package org.example.adds.Visitors;

import jakarta.persistence.*;
import lombok.Data;
import org.example.adds.Advertisement.Advertisement;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class Visitors implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String ip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Advertisement advertisement;

    @Column(nullable = false)
    private LocalDateTime visitedAt;
}
