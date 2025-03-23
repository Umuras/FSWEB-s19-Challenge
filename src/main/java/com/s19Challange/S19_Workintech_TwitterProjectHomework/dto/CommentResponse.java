package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

public record CommentResponse(Long commentId, String commentText, Long tweetId, String tweetText,
                              Long tweetUserId, String tweetUserName, String tweetUserEmail,
                              Long commentUserId, String commentUserName, String commentUserEmail) {
}
