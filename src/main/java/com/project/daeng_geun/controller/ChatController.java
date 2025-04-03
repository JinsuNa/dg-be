package com.project.daeng_geun.controller;

import com.project.daeng_geun.dto.ChatMessageDTO;
import com.project.daeng_geun.dto.ChatRoomRequestDTO;
import com.project.daeng_geun.dto.ChatRoomResponseDTO;
import com.project.daeng_geun.entity.ChatMessage;
import com.project.daeng_geun.service.ChatMessageService;
import com.project.daeng_geun.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    // WebSocket 메시지 전송 (중복 제거)
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        ChatMessage savedMessage = chatMessageService.saveMessage(
                chatMessageDTO.getChatRoomId(),
                chatMessageDTO.getSenderId(),
                chatMessageDTO.getMessage(),
                chatMessageDTO.getCreatedAt()
        );

        // 특정 채팅방에 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chat/" + chatMessageDTO.getChatRoomId(),
                ChatMessageDTO.fromEntity(savedMessage)
        );
    }

    // 특정 채팅방의 모든 메시지 조회 (REST API)
    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Long chatRoomId) {
        List<ChatMessageDTO> messages = chatMessageService.getChatMessages(chatRoomId)
                .stream()
                .map(ChatMessageDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(messages);
    }

    // 특정 채팅방의 메시지 저장 (REST API)
    @PostMapping("/message")
    public ResponseEntity<ChatMessage> saveMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessageDTO.getChatRoomId(), chatMessageDTO.getSenderId(), chatMessageDTO.getMessage(), chatMessageDTO.getCreatedAt());
        return ResponseEntity.ok(savedMessage);
    }

    // 채팅방 생성 (매칭 완료 시 자동 생성)
    @PostMapping("/create")
    public ResponseEntity<ChatRoomResponseDTO> createChatRoom(@RequestBody ChatRoomRequestDTO requestDTO) {
        ChatRoomResponseDTO chatRoom = chatRoomService.createChatRoom(requestDTO.getSenderId(), requestDTO.getReceiverId());
        return ResponseEntity.ok(chatRoom);
    }

    // sender_id, receiver_id 로 채팅방 번호 찾기
    @GetMapping("/getChatRoomId")
    public ResponseEntity<Long> getChatRoomId(@RequestParam Long senderId, @RequestParam Long receiverId) {
        Long chatRoomId = chatRoomService.getChatRoomId(senderId, receiverId);
        if (chatRoomId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatRoomId);
    }

    // 채팅방 삭제
    @DeleteMapping("/delete/{chatRoomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable Long chatRoomId) {

        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok("채팅방이 삭제되었습니다.");
    }
}
