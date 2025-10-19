package com.s19Challange.S19_Workintech_TwitterProjectHomework.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tweet", schema = "s19")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "tweet_text")
    private String tweetText;

    @Setter
    @Column(name = "image_url")
    private String imageUrl;


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "likes", schema = "s19", joinColumns = @JoinColumn(name = "tweet_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private List<Likes> likes;
@OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
private List<Likes> likes = new ArrayList<>();

    public void addLike(Likes like)
    {
        if(likes == null)
        {
            likes = new ArrayList<>();
        }
        likes.add(like);
    }

    //orphanRemoval dediğimiz zaman bu tweeti bağlı olan retweet kayıtları da silinecek
    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private List<Retweet> retweets = new ArrayList<>();

    public void addRetweet(Retweet retweet)
    {
        if(retweets == null)
        {
            retweets = new ArrayList<>();
        }
        retweets.add(retweet);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Likes> getLikes() {
        return likes;
    }

    public void setLikes(List<Likes> likes) {
        this.likes = likes;
    }

    public List<Retweet> getRetweets()
    {
        return retweets;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

}
