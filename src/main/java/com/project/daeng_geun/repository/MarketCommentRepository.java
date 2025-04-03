package com.project.daeng_geun.repository;

import com.project.daeng_geun.entity.MarketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarketCommentRepository extends JpaRepository<MarketComment, Long> {

    List<MarketComment> findByProductIdOrderByCreatedAtAsc(Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MarketComment c WHERE c.product.id = :sellerId")
    void deleteByProductId(@Param("productId") Long productId); // ✅ 진짜 삭제
}