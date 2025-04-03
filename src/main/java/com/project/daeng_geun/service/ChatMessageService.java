package com.project.daeng_geun.service;

import com.project.daeng_geun.entity.ChatMessage;
import com.project.daeng_geun.entity.ChatRoom;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.ChatMessageRepository;
import com.project.daeng_geun.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

//  채팅 메세지 저장
    @Transactional
    public ChatMessage saveMessage(Long chatRoomId, Long senderId, String messageText, LocalDateTime createdAt) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("보내는 사용자를 찾을 수 없습니다."));

        return chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(messageText)
                .createdAt(LocalDateTime.now())
                .build());
    }

    //특정 상대방의 모든 메세지 조회

    public List<ChatMessage> getChatMessages(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }


}
