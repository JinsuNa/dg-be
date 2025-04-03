package com.project.daeng_geun.controller;

import com.project.daeng_geun.dto.ChatDTO;
import com.project.daeng_geun.dto.MatchDTO;
import com.project.daeng_geun.dto.MatchRequestDTO;
import com.project.daeng_geun.dto.SenderReceiverDTO;
import com.project.daeng_geun.entity.Match;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.MatchRepository;
import com.project.daeng_geun.repository.UserRepository;
import com.project.daeng_geun.service.MatchService;
import com.project.daeng_geun.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class MatchController {
    private final MatchService matchService;
    private final UserService userService;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    @GetMapping("/random")
    public ResponseEntity<List<MatchDTO>> getRandomUsers(@RequestParam Long senderId) {
        return ResponseEntity.ok(matchService.getRandomUsers(senderId));
    }

    @PostMapping("/like/{id}")
    public MatchDTO likeCount(@PathVariable("id") Long id) {
        return matchService.likeCount(id);
    }

    @GetMapping("/like/{id}")
    public MatchDTO getLike(@PathVariable("id") Long id) {
        return matchService.getLike(id);
    }

    @GetMapping("/top-liked")
    public ResponseEntity<List<MatchDTO>> getTopLikeCount() {
        List<MatchDTO> topDogs = matchService.getTopLikeCount();
        return ResponseEntity.ok(topDogs);
    }


    @GetMapping("/doglist")
    public ResponseEntity<List<SenderReceiverDTO>> getMatch(@RequestParam Long senderId) {
        return ResponseEntity.ok(matchService.getMatch(senderId));
    }

    @PostMapping
    public ResponseEntity<ChatDTO> createMatch(@RequestBody MatchRequestDTO matchRequestDto) {
        ChatDTO chatDTO = matchService.createMatch(matchRequestDto);
        return ResponseEntity.ok(chatDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMatch(
            @RequestParam Long senderId,
            @RequestParam Long receiverId
    ) {
        matchService.deleteMatch(senderId, receiverId);
        return ResponseEntity.ok("매칭이 삭제되었습니다.");
    }
}
