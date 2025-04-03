package com.project.daeng_geun.repository;

import com.project.daeng_geun.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 카테고리별 게시글 조회 (기존)
    List<Post> findByCategory(String category);

    // 전체 게시글 최신순 정렬
    List<Post> findAllByOrderByCreatedAtDesc();

    // 카테고리별 게시글 최신순 정렬
    List<Post> findByCategoryOrderByCreatedAtDesc(String category);

}
