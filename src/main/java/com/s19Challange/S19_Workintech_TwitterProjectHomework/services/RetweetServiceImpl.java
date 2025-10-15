package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Retweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.RetweetRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.TweetRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.UserRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RetweetServiceImpl implements RetweetService{

    private RetweetRepository retweetRepository;
    private UserService userService;
    private TweetService tweetService;

    @Autowired
    public RetweetServiceImpl(RetweetRepository retweetRepository, UserService userService, TweetService tweetService)
    {
        this.retweetRepository = retweetRepository;
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @Override
    public Retweet findById(Long retweetId) {
        Optional<Retweet> optionalRetweet = retweetRepository.findById(retweetId);
        if(optionalRetweet.isPresent())
        {
            return optionalRetweet.get();
        }
        throw new TweetException("There is no retweet belong this id", HttpStatus.NOT_FOUND);
    }

    @Override
    public Long tweetRetweetCount(Long tweetId)
    {
        return retweetRepository.tweetRetweetCount(tweetId);
    }

    @Transactional
    @Override
    public Retweet save(Retweet retweet, Long tweetId) {
        User user = userService.findById(SecurityUtil.getCurrentUserId());
        Tweet tweet = tweetService.findById(tweetId);
        retweet.setUser(user);
        retweet.setTweet(tweet);

        tweet.getRetweets().forEach((retweetInfo -> {
            if(retweetInfo.getUser().getId().equals(user.getId()))
            {
                throw new TweetException("You already retweet this tweet so you can't retweet this tweet", HttpStatus.BAD_REQUEST);
            }
        }));

        return retweetRepository.save(retweet);
    }

    @Transactional
    @Override
    public void delete(Long retweetId) {
        Retweet retweet = findById(retweetId);
        if(retweet.getUser().getId().equals(SecurityUtil.getCurrentUserId()))
        {
            retweetRepository.delete(retweet);
        }
        else{
            throw new TweetException("This isn't your retweet, so you can't delete this", HttpStatus.BAD_REQUEST);
        }
    }
}
