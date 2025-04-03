package com.project.daeng_geun.service;

import com.project.daeng_geun.dto.CommentDTO;
import com.project.daeng_geun.entity.Comment;
import com.project.daeng_geun.entity.Post;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.CommentRepository;
import com.project.daeng_geun.repository.PostRepository;
import com.project.daeng_geun.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글별 댓글 조회

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 댓글 생성
    public CommentDTO createComment(Long postId, Long userId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(commentDTO.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentDTO.fromEntity(savedComment);
    }

    // 댓글 수정
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.setContent(commentDTO.getContent());
        return CommentDTO.fromEntity(commentRepository.save(comment));
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        System.out.println("요청한 유저 ID: " + userId);
        System.out.println("댓글 작성자 ID: " + comment.getUser().getId());

        if (!comment.getUser().getId().equals(userId)) {
            System.out.println("작성자가 아니므로 삭제 불가!");
            throw new SecurityException("작성자만 삭제할 수 있습니다.");
        }

        System.out.println("작성자가 맞아서 삭제 진행!");
        commentRepository.delete(comment);
    }


}
