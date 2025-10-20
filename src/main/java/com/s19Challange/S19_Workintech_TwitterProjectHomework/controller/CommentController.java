package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.CommentRequest;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.CommentResponse;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.TweetCommentCount;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Comment;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService)
    {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public CommentResponse findById(@PathVariable Long id)
    {
        Comment comment = commentService.findById(id);

        return new CommentResponse(comment.getId(), comment.getCommentText(), comment.getTweet().getId(),
                comment.getTweet().getTweetText(), comment.getTweet().getUser().getId(),
                comment.getTweet().getUser().getFirstName() + " " + comment.getTweet().getUser().getLastName(),
                comment.getTweet().getUser().getTwitterUserName(), comment.getUser().getId(),
                comment.getUser().getFirstName() + " " + comment.getUser().getLastName(),
                comment.getUser().getTwitterUserName());
    }

    @GetMapping("/quantity/{tweetId}")
    public Long findCommentsQuantity(@PathVariable Long tweetId)
    {
        return commentService.findCommentQuantity(tweetId);
    }

    @PostMapping("/{tweetId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse save(@PathVariable Long tweetId, @RequestBody CommentRequest commentRequest)
    {
        Comment rqComment = new Comment();
        rqComment.setCommentText(commentRequest.commentText());
        Comment comment = commentService.save(tweetId, rqComment);

        return new CommentResponse(comment.getId(), comment.getCommentText(), comment.getTweet().getId(),
                comment.getTweet().getTweetText(), comment.getTweet().getUser().getId(),
                comment.getTweet().getUser().getFirstName() + " " + comment.getTweet().getUser().getLastName(),
                comment.getTweet().getUser().getTwitterUserName(), comment.getUser().getId(),
                comment.getUser().getFirstName() + " " + comment.getUser().getLastName(),
                comment.getUser().getTwitterUserName());
    }

    @PatchMapping("/{id}")
    public CommentResponse update(@PathVariable Long id, @RequestBody CommentRequest commentRequest)
    {
        Comment rqComment = new Comment();
        rqComment.setCommentText(commentRequest.commentText());
        Comment uptadedComment = commentService.update(id,rqComment);

        return new CommentResponse(uptadedComment.getId(), uptadedComment.getCommentText(), uptadedComment.getTweet().getId(),
                uptadedComment.getTweet().getTweetText(), uptadedComment.getTweet().getUser().getId(),
                uptadedComment.getTweet().getUser().getFirstName() + " " + uptadedComment.getTweet().getUser().getLastName(),
                uptadedComment.getTweet().getUser().getTwitterUserName(), uptadedComment.getUser().getId(),
                uptadedComment.getUser().getFirstName() + " " + uptadedComment.getUser().getLastName(),
                uptadedComment.getUser().getTwitterUserName());
    }

    @PutMapping("/{commentId}/tweet/{tweetId}")
    public CommentResponse replaceOrCreate(@PathVariable Long commentId, @PathVariable Long tweetId, @RequestBody CommentRequest commentRequest)
    {
        Comment rqComment = new Comment();
        rqComment.setCommentText(commentRequest.commentText());
        Comment uptadedComment = commentService.replaceOrCreate(commentId, tweetId, rqComment);

        return new CommentResponse(uptadedComment.getId(), uptadedComment.getCommentText(), uptadedComment.getTweet().getId(),
                uptadedComment.getTweet().getTweetText(), uptadedComment.getTweet().getUser().getId(),
                uptadedComment.getTweet().getUser().getFirstName() + " " + uptadedComment.getTweet().getUser().getLastName(),
                uptadedComment.getTweet().getUser().getTwitterUserName(), uptadedComment.getUser().getId(),
                uptadedComment.getUser().getFirstName() + " " + uptadedComment.getUser().getLastName(),
                uptadedComment.getUser().getTwitterUserName());
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId)
    {
        commentService.delete(commentId);
    }

}
