package com.project.daeng_geun.dto;

import com.project.daeng_geun.entity.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostDTO {

    private Long postId;
    private String title;
    private String content;
    private String category;
    private String writer;     // 작성자 닉네임
    private LocalDateTime createdAt; // 작성일

    private int viewCount;    // 조회수 추가
    private int commentCount; // 댓글 수 추가

    public static PostDTO fromEntity(Post post) {
        return PostDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getUser().getNickname())   // 작성자 닉네임 세팅!
                .createdAt(post.getCreatedAt())         // 작성일 세팅!
                .category(post.getCategory())
                .viewCount(post.getViewCount())      //
                .commentCount(post.getComments() != null ? post.getComments().size() : 0) // 댓글 리스트 개수 반환
                .build();
    }
}