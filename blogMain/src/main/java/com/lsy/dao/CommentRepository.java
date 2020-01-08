package com.lsy.dao;

import com.lsy.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment,Long>{


    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

    @Query("select c.email from Comment c where c.id = ?1")
    String findCommentEmail(Long id);

    @Query("select c.nickname from Comment c where c.id = ?1")
    String selectNameById(Long id);

    @Query("select c.email from Comment c group by c.email")
    List<String> selectEmailList();

}
