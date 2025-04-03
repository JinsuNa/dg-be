package com.project.daeng_geun.repository;

import com.project.daeng_geun.entity.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//    특정 채팅방의 모든 메세지를 시간순으로 조회
    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage c WHERE c.chatRoom.id = :chatRoomId")
    void deleteByChatRoom(@Param("chatRoomId") Long chatRoomId);
}
