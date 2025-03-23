package com.s19Challange.S19_Workintech_TwitterProjectHomework.repository;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT l FROM Likes AS l WHERE l.user.id = :userId")
    List<Likes> findByUserId(Long userId);

    @Query("SELECT l FROM Likes AS l WHERE l.tweet.id = :tweetId")
    List<Likes> findByTweetId(Long tweetId);
}
