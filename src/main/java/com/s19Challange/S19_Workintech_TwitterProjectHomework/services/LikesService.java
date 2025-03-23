package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;

import java.util.List;

public interface LikesService {
    Likes findById(Long id);
    List<Likes> findByUserId();
    List<Likes> findByTweetId(Long tweetId);
    Likes save(Long tweetId, Likes like);
    void delete(Long likeId);
}
