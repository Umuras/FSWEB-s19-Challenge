package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

public record LikesResponse(Long likeId, Boolean likeCreated, Long likeUserId,
                            String likeUserName, String likeUserEmail,
                            Long tweetId, String tweetText, String tweetUserName, String tweetUserEmail) {
}
