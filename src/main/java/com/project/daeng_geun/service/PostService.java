package com.project.daeng_geun.service;

import com.project.daeng_geun.dto.PostDTO;
import com.project.daeng_geun.entity.Post;
import com.project.daeng_geun.entity.PostLike;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.PostLikeRepository;
import com.project.daeng_geun.repository.PostRepository;
import com.project.daeng_geun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository; // 추가

    // 전체 게시글 최신순 정렬
    public List<PostDTO> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
    }
    // 게시글 상세 조회 + 조회수 증가
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.setViewCount(post.getViewCount() + 1);  // 조회수 증가
        postRepository.save(post);

        return PostDTO.fromEntity(post);
    }

    // 게시글 작성
    public PostDTO createPost(PostDTO postDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .category(postDTO.getCategory())
                .viewCount(0)
                .user(user)
                .likeCount(0) // 기본값 설정
                .build();

        Post savedPost = postRepository.save(post);
        return PostDTO.fromEntity(savedPost);
    }

    // 게시글 수정
    public PostDTO updatePost(Long id, Post postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setCategory(postRequest.getCategory());

        Post updatedPost = postRepository.save(post);
        return PostDTO.fromEntity(updatedPost);
    }

    // 게시글 삭제
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자가 아닌 경우 삭제 불가
        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    // 카테고리별 게시글 최신순 정렬
    public List<PostDTO> getPostsByCategory(String category) {
        return postRepository.findByCategoryOrderByCreatedAtDesc(category).stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 좋아요 토글 메서드 추가
    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        boolean isLiked = postLikeRepository.existsByUserAndPost(user, post);

        if (isLiked) {
            postLikeRepository.deleteByUserAndPost(user, post);
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            PostLike postLike = PostLike.builder().user(user).post(post).build();
            postLikeRepository.save(postLike);
            post.setLikeCount(post.getLikeCount() + 1);
        }

        postRepository.save(post);
        return !isLiked; // 프론트엔드에서 true/false로 상태 처리 가능
    }

}
