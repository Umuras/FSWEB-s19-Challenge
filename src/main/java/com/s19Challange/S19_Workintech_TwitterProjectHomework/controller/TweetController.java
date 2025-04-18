package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.LikesTweetResponse;
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
    //TODO: Tweetlerin like edilmiş listesi düzgün gelmiyor, düzeltilecek
    private TweetService tweetService;
    private UserService userService;

    @Autowired
    public TweetController(TweetService tweetService, UserService userService)
    {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<TweetResponse> findAll()
    {
        List<Tweet> tweets = tweetService.findAll();
        List<TweetResponse> tweetResponses = new ArrayList<>();
        List<LikesTweetResponse> likesTweetResponses = new ArrayList<>();

        tweets.forEach(tweet -> {
            tweetResponses.add(new TweetResponse(tweet.getId(), tweet.getTweetText(), tweet.getUser().getFirstName(),
                    tweet.getUser().getLastName(), tweet.getUser().getEmail(), likesTweetResponses));
        });

        return tweetResponses;
    }

    @GetMapping("/user")
    public List<TweetResponse> findByUserId()
    {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Tweet> tweets = tweetService.findByUserId(userId);
        List<TweetResponse> tweetResponses = new ArrayList<>();
        List<LikesTweetResponse> likesTweetResponses = new ArrayList<>();

        tweets.forEach(tweet -> {
            if(!tweet.getLikes().isEmpty())
            {
                tweet.getLikes().forEach(like -> {
                    if(like.getTweet().getId() == tweet.getId())
                    {
                        likesTweetResponses.add(new LikesTweetResponse(like.getId(), like.getLikeCreated(), tweet.getId(),
                                tweet.getTweetText()));
                    }
                });
            }
        });

        tweets.forEach(tweet -> {
            tweetResponses.add(new TweetResponse(tweet.getId(), tweet.getTweetText(), tweet.getUser().getFirstName(),
                    tweet.getUser().getLastName(), tweet.getUser().getEmail(), likesTweetResponses));
        });

        return tweetResponses;
    }

    @GetMapping("/{id}")
    public TweetResponse findById(@PathVariable Long id)
    {
        Tweet tweet = tweetService.findById(id);
        List<LikesTweetResponse> likesTweetResponses = new ArrayList<>();
        if(!tweet.getLikes().isEmpty())
        {
            tweet.getLikes().forEach(like -> {
                        if(like.getTweet().getId() == tweet.getId()) {
                            likesTweetResponses.add(new LikesTweetResponse(like.getId(), like.getLikeCreated(), tweet.getId(),
                                    tweet.getTweetText()));
                        }
            });
        }

        TweetResponse tweetResponse = new TweetResponse(tweet.getId(), tweet.getTweetText(), tweet.getUser().getFirstName(),
                tweet.getUser().getLastName(), tweet.getUser().getEmail(), likesTweetResponses);
        return tweetResponse;
    }

    @PutMapping("/{id}")
    public TweetResponse replaceOrCreate(@PathVariable Long id, @RequestBody Tweet tweet)
    {
        Tweet rcTweet = tweetService.replaceOrCreate(id,tweet);

        List<LikesTweetResponse> likesTweetResponses = new ArrayList<>();
        if(!rcTweet.getLikes().isEmpty())
        {
            rcTweet.getLikes().forEach(like -> {
                        if(like.getTweet().getId() == rcTweet.getId()) {
                            likesTweetResponses.add(new LikesTweetResponse(like.getId(), like.getLikeCreated(), rcTweet.getId(),
                                    rcTweet.getTweetText()));
                        }
            });
        }

        TweetResponse tweetResponse = new TweetResponse(rcTweet.getId(), rcTweet.getTweetText(), rcTweet.getUser().getFirstName(),
                rcTweet.getUser().getLastName(), rcTweet.getUser().getEmail(), likesTweetResponses);
        return tweetResponse;
    }

    @PatchMapping("/{id}")
    public TweetResponse update(@PathVariable Long id, @RequestBody Tweet tweet)
    {
        Tweet updatedTweet = tweetService.update(id,tweet);
        List<LikesTweetResponse> likesTweetResponses = new ArrayList<>();
        if(!updatedTweet.getLikes().isEmpty())
        {
            updatedTweet.getLikes().forEach(like -> {
                        if(like.getTweet().getId() == updatedTweet.getId()) {
                            likesTweetResponses.add(new LikesTweetResponse(like.getId(), like.getLikeCreated(), updatedTweet.getId(),
                                    updatedTweet.getTweetText()));
                        }
            });
        }

        TweetResponse tweetResponse = new TweetResponse(updatedTweet.getId(), updatedTweet.getTweetText(), updatedTweet.getUser().getFirstName(),
                updatedTweet.getUser().getLastName(), updatedTweet.getUser().getEmail(), likesTweetResponses);
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

        Tweet savedTweet = tweetService.save(tweet);
        List<LikesTweetResponse> likesTweetResponses = new ArrayList<>();
        if(!savedTweet.getLikes().isEmpty())
        {
            savedTweet.getLikes().forEach(like -> {
                        if(like.getTweet().getId() == savedTweet.getId()) {
                            likesTweetResponses.add(new LikesTweetResponse(like.getId(), like.getLikeCreated(), savedTweet.getId(),
                                    savedTweet.getTweetText()));
                        }
            });
        }

        TweetResponse tweetResponse = new TweetResponse(savedTweet.getId(), savedTweet.getTweetText(), savedTweet.getUser().getFirstName(),
                savedTweet.getUser().getLastName(), savedTweet.getUser().getEmail(), likesTweetResponses);
        return tweetResponse;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        Tweet tweet = tweetService.findById(id);
        tweetService.delete(id);
    }
}
