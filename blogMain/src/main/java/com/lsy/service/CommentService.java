package com.lsy.service;

import com.lsy.po.Comment;

import java.util.List;


public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    String selectCommentMailById(Long id);

    String selectNameById(Long id);

    List<String> selectEmailList();

    String selectCommentTextById(Long id);
}
