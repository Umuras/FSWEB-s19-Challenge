package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService)
    {
        this.commentService = commentService;
    }


}
