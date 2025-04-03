package com.project.daeng_geun.repository;

import com.project.daeng_geun.entity.PostLike;
import com.project.daeng_geun.entity.Post;
import com.project.daeng_geun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    void deleteByUserAndPost(User user, Post post);
}
