package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

public record LikesResponse(Long likeId, Boolean likeCreated, Long likeUserId,
                            String likeFullName, String likeUserName,
                            Long tweetId, String tweetText, String tweetFullName, String tweetUserName) {
}
