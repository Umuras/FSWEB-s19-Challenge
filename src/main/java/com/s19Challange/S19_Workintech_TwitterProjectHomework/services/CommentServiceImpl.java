package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.TweetCommentCount;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Comment;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.CommentRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;
    private UserService userService;
    private TweetService tweetService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, TweetService tweetService)
    {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @Transactional
    @Override
    public Comment findById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if(optionalComment.isPresent())
        {
            return optionalComment.get();
        }
        throw new TweetException("There is no comment on this id", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public Comment save(Long tweetId, Comment comment) {
        User user = userService.findById(SecurityUtil.getCurrentUserId());
        Tweet tweet = tweetService.findById(tweetId);
        comment.setUser(user);
        comment.setTweet(tweet);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment replaceOrCreate(Long commentId, Long tweetId, Comment comment) {
        Optional<Comment> updatedComment = commentRepository.findById(commentId);
        if(updatedComment.isPresent())
        {
            if(!updatedComment.get().getUser().getId().equals(SecurityUtil.getCurrentUserId()))
            {
                throw new TweetException("You can't update this comment because it isn't your comment", HttpStatus.BAD_REQUEST);
            }

            if (!updatedComment.get().getTweet().getId().equals(tweetId))
            {
                throw new TweetException("You can't update this comment because you send wrong tweet id", HttpStatus.BAD_REQUEST);
            }

            updatedComment.get().setCommentText(comment.getCommentText());
            return commentRepository.save(updatedComment.get());
        }

        Tweet tweet = tweetService.findById(tweetId);
        if(tweet == null)
        {
            throw new TweetException("You can't create this comment because you send wrong tweet id", HttpStatus.BAD_REQUEST);
        }
        User user = userService.findById(SecurityUtil.getCurrentUserId());
        comment.setUser(user);
        comment.setTweet(tweet);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(Long commentId, Comment comment) {
        Comment updatedComment = findById(commentId);
        if(!updatedComment.getUser().getId().equals(SecurityUtil.getCurrentUserId()))
        {
            throw new TweetException("You can't update this comment because it isn't your comment", HttpStatus.BAD_REQUEST);
        }
        if(comment.getCommentText() != null)
        {
            updatedComment.setCommentText(comment.getCommentText());
        }
        return commentRepository.save(updatedComment);
    }

    @Override
    public Long findCommentQuantity(Long tweetId) {
        return commentRepository.findCommentQuantity(tweetId);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Comment comment = findById(id);
        if(comment.getUser().getId().equals(SecurityUtil.getCurrentUserId())  ||
                comment.getTweet().getUser().getId().equals(SecurityUtil.getCurrentUserId()))
        {
            commentRepository.delete(comment);
        }else{
            throw new TweetException("This comment or tweet is not yours, so you don't delete this comment", HttpStatus.BAD_REQUEST);
        }
    }
}
