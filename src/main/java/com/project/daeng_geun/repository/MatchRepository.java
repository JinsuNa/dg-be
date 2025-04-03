package com.project.daeng_geun.repository;

import com.project.daeng_geun.entity.Match;
import com.project.daeng_geun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findBySenderAndReceiverOrderByCreatedAtAsc(User Sender, User receiver);

    @Query("SELECT m.receiver.id FROM Match m WHERE m.sender.id = :senderId")
    List<Long> findReceiverIdsBySender(@Param("senderId") Long senderId);

    // 내가 보낸 요청
    List<Match> findBySender(User sender);

    // 내가 받은 요청
    List<Match> findByReceiver(User receiver);

    // 내가 보낸 요청 + 내가 받은 요청을 한 번에 가져오기 (Native Query 사용)
    @Query("SELECT m FROM Match m WHERE m.sender = :user OR m.receiver = :user")
    List<Match> findBySenderOrReceiver(@Param("user") User user);

//    삭제요청
    @Query("SELECT m FROM Match m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) OR (m.sender.id = :receiverId AND m.receiver.id = :senderId)")
    List<Match> findMatchByUsers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
