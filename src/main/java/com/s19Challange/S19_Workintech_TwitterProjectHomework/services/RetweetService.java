package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Retweet;

public interface RetweetService {
    Retweet findById(Long retweetId);
    Long tweetRetweetCount(Long tweetId);
    Retweet save(Retweet retweet, Long tweetId);
    void delete(Long retweetId);
}
