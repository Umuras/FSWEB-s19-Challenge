package com.s19Challange.S19_Workintech_TwitterProjectHomework.repository;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    @Query("SELECT t FROM Tweet t WHERE t.user.id = :userid")
    List<Tweet> findByUserId(@Param("userid") Long userid);
}
