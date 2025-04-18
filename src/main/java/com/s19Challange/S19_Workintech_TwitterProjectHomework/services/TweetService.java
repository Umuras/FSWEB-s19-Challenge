package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;

import java.util.List;

public interface TweetService {
    List<Tweet> findAll();
    List<Tweet> findByUserId(Long userid);
    Tweet findById(Long tweetId);
    Tweet replaceOrCreate(Long tweetId, Tweet tweet);
    Tweet update(Long tweetId, Tweet tweet);
    Tweet save(Tweet tweet);
    void delete(Long id);
}
