package com.project.daeng_geun.dto;

import com.project.daeng_geun.entity.Match;
import com.project.daeng_geun.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ChatDTO {
    private Long senderId;    // User 전체가 아니라 ID만
    private Long receiverId;
    private String status;

    public static ChatDTO from(Match match) {
        return ChatDTO.builder()
                .senderId(match.getSender().getId())       // ✔ ID만 넣기
                .receiverId(match.getReceiver().getId())   // ✔ ID만 넣기
                .status(match.getStatus())
                .build();
    }
}