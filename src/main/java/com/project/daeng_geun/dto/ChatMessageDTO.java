package com.project.daeng_geun.dto;

import com.project.daeng_geun.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Long id;
    private Long chatRoomId; // ✅ 채팅방 ID만 포함 (중복 제거)
    private Long senderId;   // ✅ sender ID만 포함 (중복 제거)
    private String message;
    private LocalDateTime createdAt;

    // ✅ ChatMessage 엔티티 → ChatMessageDTO 변환
    public static ChatMessageDTO fromEntity(ChatMessage chatMessage) {
        return ChatMessageDTO.builder()
                .id(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoom().getId())  // 채팅방 ID만 포함
                .senderId(chatMessage.getSender().getId())  // sender의 ID만 포함
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}