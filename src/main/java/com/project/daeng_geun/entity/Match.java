package com.project.daeng_geun.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(name = "matches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnoreProperties({"sentMessages", "receivedMessages", "hibernateLazyInitializer"}) // 🚀 sender 정보를 포함하도록 설정
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonIgnoreProperties({"sentMessages", "receivedMessages", "hibernateLazyInitializer"}) // 🚀 receiver 정보를 포함하도록 설정
    private User receiver;

    @Column(nullable = false)
    private String status; // SENT, DELIVERED, READ

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = "SENT"; // 기본값
    }

}