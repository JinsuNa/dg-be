package com.project.daeng_geun.controller;

import com.project.daeng_geun.dto.CommentDTO;
import com.project.daeng_geun.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 게시글별 댓글 조회

    @GetMapping("/post/{postId}")
    public List<CommentDTO> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    // 댓글 작성

    @PostMapping("/post/{postId}")
    public CommentDTO createComment(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestBody CommentDTO commentDTO) {

        return commentService.createComment(postId, userId, commentDTO);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public CommentDTO updateComment(@PathVariable Long commentId,
                                    @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(commentId, commentDTO);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }



}
