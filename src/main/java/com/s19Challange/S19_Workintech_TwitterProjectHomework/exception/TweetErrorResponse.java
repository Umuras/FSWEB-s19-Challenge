package com.s19Challange.S19_Workintech_TwitterProjectHomework.exception;

import lombok.Data;


@Data
public class TweetErrorResponse {
    private int status;
    private String message;
    private long timestamp;

    public TweetErrorResponse(int status, String message, long timestamp)
    {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus()
    {
        return status;
    }

    public String getMessage()
    {
        return message;
    }

    public long getTimeStamp()
    {
        return timestamp;
    }
}
