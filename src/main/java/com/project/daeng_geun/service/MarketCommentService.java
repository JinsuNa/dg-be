package com.project.daeng_geun.service;

import com.project.daeng_geun.entity.MarketComment;
import com.project.daeng_geun.entity.Product;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.MarketCommentRepository;
import com.project.daeng_geun.repository.ProductRepository;
import com.project.daeng_geun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketCommentService {

    private final MarketCommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public MarketComment createComment(Long productId, Long userId, String content) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        MarketComment comment = MarketComment.builder()
                .product(product)
                .user(user)
                .content(content)
                .build();

        return commentRepository.save(comment);
    }

    public List<MarketComment> getComments(Long productId) {
        return commentRepository.findByProductIdOrderByCreatedAtAsc(productId);
    }

    public void deleteComment(Long commentId, Long userId) {
        MarketComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getUser().getId().equals(userId)) {
            throw new SecurityException("본인 댓글만 삭제할 수 있습니다.");
        }
        // 댓글 먼저 삭제
//        commentRepository.deleteByProductId(commentId);
        commentRepository.delete(comment);
    }

}
