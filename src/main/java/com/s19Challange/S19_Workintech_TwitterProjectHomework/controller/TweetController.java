package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.TweetResponse;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
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
public class TweetController {

    private TweetService tweetService;
    private UserService userService;

    @Autowired
    public TweetController(TweetService tweetService, UserService userService)
    {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @GetMapping("/welcome")
    public String hi()
    {
        return "Merhaba benim idim: " + SecurityUtil.getCurrentUserId();
    }

    @GetMapping("/user")
    public List<TweetResponse> findByUserId()
    {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Tweet> tweets = tweetService.findByUserId(userId);
        List<TweetResponse> tweetResponses = new ArrayList<>();

        tweets.forEach(tweet -> {
            tweetResponses.add(new TweetResponse(tweet.getTweetText(), tweet.getUser().getFirstName(),
                    tweet.getUser().getLastName(), tweet.getUser().getEmail(), tweet.getLikes()));
        });

        return tweetResponses;
    }

    @GetMapping("/{id}")
    public TweetResponse findById(@PathVariable Long id)
    {
        Tweet tweet = tweetService.findById(id);
        TweetResponse tweetResponse = new TweetResponse(tweet.getTweetText(), tweet.getUser().getFirstName(),
                tweet.getUser().getLastName(), tweet.getUser().getEmail(), tweet.getLikes());
        return tweetResponse;
    }

    @PutMapping("/{id}")
    public TweetResponse replaceOrCreate(@PathVariable Long id, @RequestBody Tweet tweet)
    {
        Tweet rcTweet = tweetService.replaceOrCreate(id,tweet);
        TweetResponse tweetResponse = new TweetResponse(rcTweet.getTweetText(), rcTweet.getUser().getFirstName(),
                rcTweet.getUser().getLastName(), rcTweet.getUser().getEmail(), rcTweet.getLikes());
        return tweetResponse;
    }

    @PatchMapping("/{id}")
    public TweetResponse update(@PathVariable Long id, @RequestBody Tweet tweet)
    {
        Tweet updatedTweet = tweetService.update(id,tweet);
        TweetResponse tweetResponse = new TweetResponse(updatedTweet.getTweetText(), updatedTweet.getUser().getFirstName(),
                updatedTweet.getUser().getLastName(), updatedTweet.getUser().getEmail(), updatedTweet.getLikes());
        tweetService.save(updatedTweet);
        return tweetResponse;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponse save(@RequestBody Tweet tweet)
    {
        User currentUser = userService.findById(SecurityUtil.getCurrentUserId());
        tweet.setUser(currentUser);

        if(tweet.getUser() == null)
        {
            throw new TweetException("User not null!!!", HttpStatus.BAD_REQUEST);
        }

        TweetResponse tweetResponse = new TweetResponse(tweet.getTweetText(), tweet.getUser().getFirstName(),
                tweet.getUser().getLastName(), tweet.getUser().getEmail(), tweet.getLikes());
        tweetService.save(tweet);
        return tweetResponse;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        Tweet tweet = tweetService.findById(id);
        tweetService.delete(id);
    }
}
