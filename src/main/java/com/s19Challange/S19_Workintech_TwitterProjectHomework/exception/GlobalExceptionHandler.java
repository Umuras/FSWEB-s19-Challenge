package com.s19Challange.S19_Workintech_TwitterProjectHomework.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TweetException.class)
    public ResponseEntity<TweetErrorResponse> handleException(TweetException tweetException)
    {
        TweetErrorResponse errorResponse = new TweetErrorResponse(tweetException.getHttpStatus().value(),
                tweetException.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<TweetErrorResponse>(errorResponse,tweetException.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TweetErrorResponse> handleException(Exception exception)
    {
        TweetErrorResponse errorResponse = new TweetErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<TweetErrorResponse>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
