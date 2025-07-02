package com.s19Challange.S19_Workintech_TwitterProjectHomework.repository;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    @Query("SELECT COUNT(r.id) FROM Retweet AS r WHERE r.tweet.id =:tweetId")
    Long tweetRetweetCount(Long tweetId);
}
