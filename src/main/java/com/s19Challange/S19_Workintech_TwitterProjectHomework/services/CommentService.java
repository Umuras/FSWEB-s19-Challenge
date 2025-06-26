package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.TweetCommentCount;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment findById(Long id);
    Comment save(Long tweetId, Comment comment);
    Comment replaceOrCreate(Long commentId, Long tweetId, Comment comment);
    Comment update(Long commentId, Comment comment);
    List<TweetCommentCount> findCommentQuantity();
    void delete(Long id);
}
