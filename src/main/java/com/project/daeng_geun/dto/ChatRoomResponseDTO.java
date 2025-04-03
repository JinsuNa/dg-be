package com.project.daeng_geun.dto;

import com.project.daeng_geun.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomResponseDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String receiverNickname;


    public ChatRoomResponseDTO(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.senderId = chatRoom.getSender().getId();
        this.receiverId = chatRoom.getReceiver().getId();
    }
}