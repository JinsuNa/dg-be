package com.project.daeng_geun.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) // ✅ Auditing 적용
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 2000)
    private String description;

    private String location;

    private String image;

    private int views ;  // ✅ 조회수 필드 추가

    private Long lastViewedTime; // 🕒 마지막 조회 시간 (Unix timestamp)

    public boolean isViewedRecently() {
        long currentTime = System.currentTimeMillis();
        if (lastViewedTime == null) return false;
        return (currentTime - lastViewedTime) < 2_000; // 2초 이내면 true 반환
    }

    // 필요 시 setter/getter 추가
    public void setLastViewedTime(Long time) {
        this.lastViewedTime = time;
    }

    public Long getLastViewedTime() {
        return lastViewedTime;
    }
    @CreatedDate // ✅ 생성 날짜 자동 입력
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // ✅ 업데이트 날짜 자동 입력
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MarketComment> comments;

}
