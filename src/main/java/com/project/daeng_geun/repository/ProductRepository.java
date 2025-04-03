package com.project.daeng_geun.repository;

import com.project.daeng_geun.entity.Product;
import jakarta.transaction.Transactional;
import org.eclipse.angus.mail.imap.protocol.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"seller"})  // ✅ seller 정보도 함께 불러오기
    List<Product> findAll();

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.views = p.views + 1 WHERE p.id = :productId")
    void incrementViews(@Param("productId") Long productId);
}
