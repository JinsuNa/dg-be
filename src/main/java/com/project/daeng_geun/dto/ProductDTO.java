package com.project.daeng_geun.dto;

import com.project.daeng_geun.entity.Product;
import com.project.daeng_geun.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String title;
    private Integer price;
    private String description;
    private String location;  // 사용자 위치 정보 추가
    private String image;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long sellerId;
    private String sellerName;
    private String sellerNickname; // 판매자의 닉네임 추가
    private String sellerImage;    // 판매자의 프로필 이미지 추가

    // Entity → DTO 변환 메서드
    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .image(product.getImage())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .views(product.getViews())
                .sellerId(product.getSeller().getId())
                .sellerName(product.getSeller().getNickname())
                .sellerNickname(product.getSeller().getNickname()) // User 엔티티에서 닉네임 가져오기
                .sellerImage(product.getSeller().getImage()) // User 엔티티에서 프로필 이미지 가져오기
                .location(product.getLocation())  // 사용자 위치 추가
                .build();
    }

    // DTO → Entity 변환 메서드
    public Product toEntity(User seller) {
        return Product.builder()
                .title(this.title)
                .price(this.price)
                .description(this.description)
                .image(this.image)
                .seller(seller)
                .location(seller.getLocation())  // User의 위치 정보 저장
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
