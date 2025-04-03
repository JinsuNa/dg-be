package com.project.daeng_geun.repository;

import com.project.daeng_geun.entity.ChatRoom;
import com.project.daeng_geun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE (c.sender = :user1 AND c.receiver = :user2) OR (c.sender = :user2 AND c.receiver = :user1)")
    Optional<ChatRoom> findChatRoomByUsers(@Param("user1") User user1, @Param("user2") User user2);



    List<ChatRoom> findBySenderOrReceiver(User sender, User receiver);
    Optional<ChatRoom> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    @Query("SELECT c FROM ChatRoom c WHERE (c.sender.id = :user1 AND c.receiver.id = :user2) OR (c.sender.id = :user2 AND c.receiver.id = :user1)")
    List<ChatRoom> findChatRoomByUsers(@Param("user1") Long user1, @Param("user2") Long user2);
}
