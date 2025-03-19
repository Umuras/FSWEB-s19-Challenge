package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAll();
    Comment findById(Long id);
    Comment save(Comment comment);
    void delete(Long id);
}
