package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

public record TweetCommentResponse(Long commentId, String commentText, Long tweetId, String tweetText, Long userId, String userName) {
}
