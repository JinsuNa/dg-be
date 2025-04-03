package com.project.daeng_geun.controller;

import com.project.daeng_geun.dto.MarketCommentDTO;
import com.project.daeng_geun.entity.MarketComment;
import com.project.daeng_geun.service.MarketCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market-comments")
@RequiredArgsConstructor
public class MarketCommentController {

    private final MarketCommentService commentService;

    @PostMapping
    public ResponseEntity<MarketComment> createComment(@RequestBody MarketCommentDTO request) {
        MarketComment comment = commentService.createComment(
                request.getProductId(),
                request.getUserId(),
                request.getContent()
        );
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<MarketCommentDTO>> getComments(@PathVariable Long productId) {
        List<MarketComment> comments = commentService.getComments(productId);

        List<MarketCommentDTO> response = comments.stream()
                .map(c -> new MarketCommentDTO(
                        c.getId(),
                        c.getContent(),
                        c.getCreatedAt().toString(),
                        c.getProduct().getId(),
                        c.getUser().getId(),
                        c.getUser().getNickname()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

//    @PutMapping("/{commentId}")
//    public ResponseEntity<Void> updateComment(
//            @PathVariable Long commentId,
//            @RequestBody MarketCommentDTO request
//    ) {
//        commentService.updateComment(commentId, request.getUserId(), request.getContent());
//        return ResponseEntity.ok().build();
//    }

}
