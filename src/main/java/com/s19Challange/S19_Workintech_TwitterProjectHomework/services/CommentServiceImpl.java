package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Comment;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findAll() {
        return List.of();
    }

    @Override
    public Comment findById(Long id) {
        return null;
    }

    @Override
    public Comment save(Comment comment) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
