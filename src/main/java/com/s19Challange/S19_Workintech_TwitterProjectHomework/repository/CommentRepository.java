package com.s19Challange.S19_Workintech_TwitterProjectHomework.repository;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.TweetCommentCount;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT COUNT(c.id) AS comment_count FROM s19.comment AS c WHERE c.tweet_Id=:tweetId", nativeQuery = true)
    Long findCommentQuantity(Long tweetId);
}
