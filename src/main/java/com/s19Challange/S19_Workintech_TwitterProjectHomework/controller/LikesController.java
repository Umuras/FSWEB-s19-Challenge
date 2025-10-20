package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.LikesResponse;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.LikesTweetResponse;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.LikesService;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.TweetService;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/tweet")
public class LikesController {

    private LikesService likesService;
    private UserService userService;
    private TweetService tweetService;

    @Autowired
    public LikesController(LikesService likesService, UserService userService, TweetService tweetService){
        this.likesService = likesService;
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @GetMapping("/like/{id}")
    public LikesResponse findById(@PathVariable Long id)
    {
        Likes like = likesService.findById(id);
        return new LikesResponse(like.getId(), like.getLikeCreated(), like.getUser().getId(),
                like.getUser().getFirstName() + " " + like.getUser().getLastName(),
                like.getUser().getTwitterUserName(), like.getTweet().getId(), like.getTweet().getTweetText(),
                like.getTweet().getUser().getFirstName() + " " + like.getTweet().getUser().getLastName(),
                like.getTweet().getUser().getTwitterUserName());
    }

    @GetMapping("/likes/user/")
    public List<LikesResponse> findByUserId()
    {
        List<Likes> likesList = likesService.findByUserId();
        List<LikesResponse> likesResponses = new ArrayList<>();

        likesList.forEach(like -> {
            likesResponses.add(new LikesResponse(like.getId(), like.getLikeCreated(), like.getUser().getId(),
                    like.getUser().getFirstName() + " " + like.getUser().getLastName(),
                    like.getUser().getTwitterUserName(), like.getTweet().getId(), like.getTweet().getTweetText(),
                    like.getTweet().getUser().getFirstName() + " " + like.getTweet().getUser().getLastName(),
                    like.getTweet().getUser().getTwitterUserName()));
        });

        return likesResponses;
    }

    @GetMapping("/likes/{tweetId}")
    public List<LikesTweetResponse> findByTweetId(@PathVariable Long tweetId)
    {
        List<Likes> likesList = likesService.findByTweetId(tweetId);
        List<LikesTweetResponse> likesTweetResponses = new ArrayList<>();
        likesList.forEach(like -> {
            likesTweetResponses.add(new LikesTweetResponse(like.getId(),like.getLikeCreated(),like.getTweet().getId(),
                    like.getTweet().getTweetText(), like.getUser().getId(), like.getUser().getTwitterUserName(), like.getUser().getFirstName() + " " +
                    like.getUser().getLastName()));
        });
        return likesTweetResponses;
    }

    @PostMapping("/like/{tweetId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LikesResponse save(@PathVariable Long tweetId, @RequestBody Likes like)
    {
        Likes savedLike = likesService.save(tweetId,like);

        return new LikesResponse(savedLike.getId(), savedLike.getLikeCreated(), savedLike.getUser().getId(),
                savedLike.getUser().getFirstName() + " " + savedLike.getUser().getLastName(),
                savedLike.getUser().getTwitterUserName(), savedLike.getTweet().getId(), savedLike.getTweet().getTweetText(),
                savedLike.getTweet().getUser().getFirstName() + " " + savedLike.getTweet().getUser().getLastName(),
                savedLike.getTweet().getUser().getTwitterUserName());
    }

    @PostMapping("/dislike/{likeId}")
    public LikesResponse delete(@PathVariable Long likeId)
    {
        Likes like = likesService.findById(likeId);
        likesService.delete(likeId);
        return new LikesResponse(like.getId(), like.getLikeCreated(), like.getUser().getId(),
                like.getUser().getFirstName() + " " + like.getUser().getLastName(),
                like.getUser().getTwitterUserName(), like.getTweet().getId(), like.getTweet().getTweetText(),
                like.getTweet().getUser().getFirstName() + " " + like.getTweet().getUser().getLastName(),
                like.getTweet().getUser().getTwitterUserName());
    }
}
