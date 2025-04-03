package com.project.daeng_geun.service;

import com.project.daeng_geun.dto.ChatRoomResponseDTO;
import com.project.daeng_geun.entity.ChatRoom;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.ChatMessageRepository;
import com.project.daeng_geun.repository.ChatRoomRepository;
import com.project.daeng_geun.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

//    채팅방 생성
    @Transactional
    public ChatRoomResponseDTO createChatRoom(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("보내는 사용자를 찾을 수 없습니다."));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("받는 사용자를 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.findChatRoomByUsers(sender, receiver)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .build()));
        return new ChatRoomResponseDTO(chatRoom);
    }

    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
    }

    // 특정 채팅방 삭제 (관련된 메시지도 함께 삭제)
    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        // 해당 채팅방의 메시지도 모두 삭제
        chatMessageRepository.deleteByChatRoom(chatRoomId);

        // 채팅방 삭제
        chatRoomRepository.delete(chatRoom);
    }

    //    FindFriend.js 에서 거절을 누르면 채팅방까지 삭제가 되어야 하므로 우선 채팅방 번호를 찾는 기능
    public Long getChatRoomId(Long senderId, Long receiverId) {
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getId)
                .orElse(null);
    }
}
