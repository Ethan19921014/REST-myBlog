package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


// @Repository no need to annotation
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Create custom query method
    List<Comment> findCommentByPostId(long postId);

}
