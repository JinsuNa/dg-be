package com.project.daeng_geun.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.daeng_geun.entity.Match;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.MatchRepository;
import com.project.daeng_geun.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatService extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    //    사용자별 세션을 저장할 맵
    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.put(userId, session);
            log.info("사용자 {} 채팅 연결됨",userId);
        }
    }

    @Override
    @Transactional
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Match chatMessage = objectMapper.readValue(message.getPayload(), Match.class);
        chatMessage.setCreatedAt(LocalDateTime.now());

        User sender = userRepository.findById(chatMessage.getSender().getId()).orElse(null);
        User receiver = userRepository.findById(chatMessage.getReceiver().getId()).orElse(null);

        if (sender == null || receiver == null) {
            log.warn("발신자 또는 수신자가 존재하지 않습니다.");
            return;
        }
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setStatus("SENT");

//        db에 저장
        matchRepository.save(chatMessage);

//        상대방이 접속 중이면 실시간으로 전송
        WebSocketSession receiverSession = sessions.get(receiver.getId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            log.info("사용자 {}에게 메세지 전송됨", receiver.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.remove(userId);
            log.info("사용자 {} 채팅 연결 종료됨", userId);
        }
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        return null;
//        세션에서 사용자 id 추출하는 로직 (나중에 구현해야함)
    }

}
