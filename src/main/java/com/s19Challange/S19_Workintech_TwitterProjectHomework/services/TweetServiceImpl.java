package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.TweetRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetServiceImpl implements TweetService{

    private TweetRepository tweetRepository;
    private UserService userService;

    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository, UserService userService)
    {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
    }

    @Override
    public List<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    @Override
    public List<Tweet> findByUserId(Long userid) {
        List<Tweet> userTweets = tweetRepository.findByUserId(userid);
        if(!userTweets.isEmpty())
        {
            return userTweets;
        }
        throw new TweetException("This user hasn't any tweet", HttpStatus.NOT_FOUND);
    }

    @Override
    public Tweet findById(Long tweetId) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
        if(optionalTweet.isPresent())
        {
            return optionalTweet.get();
        }
        throw new TweetException("There is not a tweet this id",HttpStatus.NOT_FOUND);
    }

    @Override
    public Tweet replaceOrCreate(Long tweetId, Tweet tweet) {
        Optional<Tweet> updatedTweet = tweetRepository.findById(tweetId);
        User user = userService.findById(SecurityUtil.getCurrentUserId());

        if(updatedTweet.isPresent())
        {
            if(SecurityUtil.getCurrentUserId() != updatedTweet.get().getUser().getId())
            {
                throw new TweetException("This tweet doesn't belong to this user", HttpStatus.BAD_REQUEST);
            }
            tweet.setId(tweetId);
            tweet.setUser(user);
            return tweetRepository.save(tweet);
        }

        tweet.setUser(user);
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet update(Long tweetId, Tweet tweet) {
        Tweet updatedTweet = findById(tweetId);

        if(SecurityUtil.getCurrentUserId() != updatedTweet.getUser().getId())
        {
            throw new TweetException("This tweet doesn't belong to this user", HttpStatus.BAD_REQUEST);
        }

        if(tweet.getTweetText() != null)
        {
            updatedTweet.setTweetText(tweet.getTweetText());
        }
        if(tweet.getUser() != null)
        {
            updatedTweet.setUser(tweet.getUser());
        }
        if(tweet.getLikes() != null)
        {
            updatedTweet.setLikes(tweet.getLikes());
        }
        updatedTweet.setId(tweetId);
        return updatedTweet;
    }

    @Override
    public Tweet save(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    public void delete(Long id) {
        Tweet tweet = findById(id);
        Long userId = SecurityUtil.getCurrentUserId();
        if(userId != tweet.getUser().getId())
        {
            throw new TweetException("This tweet is not yours, so you don't delete this tweet",HttpStatus.BAD_REQUEST);
        }
        tweetRepository.delete(tweet);
    }
}
