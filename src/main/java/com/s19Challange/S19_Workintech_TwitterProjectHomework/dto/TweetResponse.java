package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;

import java.util.List;

public record TweetResponse(Long tweetId, String tweetText, Long userId, String userFirstName, String userLastName, String email,
                            List<LikesTweetResponse> likes, Long commentCount, Long likeCount, Long retweetCount) {
}
