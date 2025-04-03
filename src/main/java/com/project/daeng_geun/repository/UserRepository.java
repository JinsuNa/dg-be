package com.project.daeng_geun.repository;

import com.project.daeng_geun.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);


    //    Native Query 사용

    @Query(value = "SELECT * FROM users WHERE id NOT IN (:excludedIds) ORDER BY RAND() LIMIT 2", nativeQuery = true)
    List<User> findRandomUsersExcluding(@Param("excludedIds") List<Long> excludedIds);


    //    main페이지에 노출될 like를 받은 top3 댕댕이
    @Query(value = "SELECT * FROM users ORDER BY like_count DESC LIMIT 3", nativeQuery = true)
    List<User> topLikeCount();

}
