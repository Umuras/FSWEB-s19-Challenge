package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.LikesRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikesServiceImpl implements LikesService{

    private LikesRepository likesRepository;
    private UserService userService;
    private TweetService tweetService;

    @Autowired
    public LikesServiceImpl(LikesRepository likesRepository, UserService userService, TweetService tweetService)
    {
        this.likesRepository = likesRepository;
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @Override
    public Likes findById(Long id) {
        Optional<Likes> optionalLikes = likesRepository.findById(id);

        return optionalLikes.orElseThrow(() ->
         new TweetException("There isn't like belong this id", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Likes> findByUserId() {
        List<Likes> likesList = likesRepository.findByUserId(SecurityUtil.getCurrentUserId());
        return likesList;
    }

    @Override
    public List<Likes> findByTweetId(Long tweetId) {
        List<Likes> likesList = likesRepository.findByTweetId(tweetId);
        return likesList;
    }

    @Override
    public Long tweetLikesCount(Long tweetId) {
        return likesRepository.tweetLikesCount(tweetId);
    }

    @Override
    public Likes save(Long tweetId, Likes like) {
        User user = userService.findById(SecurityUtil.getCurrentUserId());
        Tweet tweet = tweetService.findById(tweetId);
        like.setUser(user);
        like.setTweet(tweet);

        tweet.getLikes().forEach((likeInfo) -> {
            if(likeInfo.getUser().getId().equals(user.getId()))
            {
                throw new TweetException("You already liked this tweet, so you can't like this tweet", HttpStatus.BAD_REQUEST);
            }
        });

        return likesRepository.save(like);
    }

    @Transactional
    @Override
    public void delete(Long likeId) {
        Likes like = findById(likeId);
        if(like.getUser().getId().equals(SecurityUtil.getCurrentUserId()))
        {
            likesRepository.delete(like);
        }else{
            throw new TweetException("This like isn't yours so you don't remove this like", HttpStatus.BAD_REQUEST);
        }
    }
}
