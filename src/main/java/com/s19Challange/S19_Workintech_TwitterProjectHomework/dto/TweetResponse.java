package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;

import java.util.List;

public record TweetResponse(String tweetText, String userFirstName, String userLastName, String email,
                            List<Likes> likes) {
}
