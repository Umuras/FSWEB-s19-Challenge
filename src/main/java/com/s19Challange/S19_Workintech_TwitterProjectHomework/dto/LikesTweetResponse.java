package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

public record LikesTweetResponse(Long likeId, Boolean likeCreated, Long tweetId, String tweetText, Long userId, String userName, String userFullName) {
}
