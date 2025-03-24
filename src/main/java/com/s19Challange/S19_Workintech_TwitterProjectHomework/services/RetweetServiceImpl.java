package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Retweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.RetweetRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RetweetServiceImpl implements RetweetService{

    private RetweetRepository retweetRepository;

    @Autowired
    public RetweetServiceImpl(RetweetRepository retweetRepository)
    {
        this.retweetRepository = retweetRepository;
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
    public Retweet save(Retweet retweet) {
        return retweetRepository.save(retweet);
    }

    @Override
    public void delete(Long retweetId) {
        Retweet retweet = findById(retweetId);
        if(retweet.getUser().getId() == SecurityUtil.getCurrentUserId())
        {
            retweetRepository.delete(retweet);
        }
        else{
            throw new TweetException("This isn't your retweet, so you can't delete this", HttpStatus.BAD_REQUEST);
        }
    }
}
