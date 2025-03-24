package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

public record RetweetResponse(Long retweetId, Boolean retweetCreated, Long userId, String userEmail,
                              Long tweetId, String tweetText) {
}
