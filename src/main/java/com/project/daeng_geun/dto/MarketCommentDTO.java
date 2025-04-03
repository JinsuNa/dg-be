package com.project.daeng_geun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketCommentDTO {
    // 공통 또는 응답 전용
    private Long id;
    private String content;
    private String createdAt;

    // 등록 요청 시 사용
    private Long productId;
    private Long userId;

    // 응답 시 사용
    private String nickname;
}
