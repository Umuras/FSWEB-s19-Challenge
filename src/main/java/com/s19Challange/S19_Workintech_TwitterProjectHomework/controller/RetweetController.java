package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.RetweetResponse;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Retweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.RetweetService;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.TweetService;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private RetweetService retweetService;
    private UserService userService;
    private TweetService tweetService;

    @Autowired
    public RetweetController(RetweetService retweetService, UserService userService, TweetService tweetService)
    {
        this.retweetService = retweetService;
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @GetMapping("/{id}")
    public RetweetResponse findById(@PathVariable Long id)
    {
        Retweet retweet = retweetService.findById(id);
        return new RetweetResponse(retweet.getId(), retweet.getRetweetCreated(), retweet.getUser().getId(),
                retweet.getUser().getEmail(), retweet.getTweet().getId(), retweet.getTweet().getTweetText());
    }


    @PostMapping("/{tweetId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RetweetResponse save(@PathVariable Long tweetId, @RequestBody Retweet retweet)
    {
        Retweet saveRetweet = retweetService.save(retweet, tweetId);

        return new RetweetResponse(saveRetweet.getId(), saveRetweet.getRetweetCreated(), saveRetweet.getUser().getId(),
                saveRetweet.getUser().getEmail(), saveRetweet.getTweet().getId(), saveRetweet.getTweet().getTweetText());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        retweetService.delete(id);
    }

}
