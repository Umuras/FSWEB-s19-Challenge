package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Retweet;

public interface RetweetService {
    Retweet findById(Long retweetId);
    Retweet save(Retweet retweet);
    void delete(Long retweetId);
}
