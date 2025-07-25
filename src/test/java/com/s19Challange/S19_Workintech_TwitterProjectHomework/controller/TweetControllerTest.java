package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.JwtUtil;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TweetController.class)
@AutoConfigureMockMvc(addFilters = false)
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtils;

    @MockitoBean
    private TweetService tweetService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private LikesService likesService;

    @MockitoBean
    private RetweetService retweetService;

    private Tweet tweet;
    private Tweet tweet2;
    private User user;
    private List<Tweet> tweetList;
    private List<Likes> tweetLikes;

    @BeforeEach
    void setup()
    {
        tweet = new Tweet();
        tweet.setId(1L);
        tweet.setTweetText("Hello Test");

        tweet2 = new Tweet();
        tweet2.setId(2L);
        tweet2.setTweetText("Hello Test2");

        tweetList = new ArrayList<>();
        tweetList.add(tweet);
        tweetList.add(tweet2);

        tweetLikes = new ArrayList<>();

        user = new User();
        user.setFirstName("Ali Umur");
        user.setLastName("Kucur");
        user.setId(1L);
        user.addTweet(tweet);
        user.addTweet(tweet2);
        tweet.setUser(user);
        tweet2.setUser(user);
        tweet.setLikes(tweetLikes);
        tweet2.setLikes(tweetLikes);
    }


    @DisplayName("Tüm tweetler geldi mi?")
    @Test
    void findAll() throws Exception {
        when(tweetService.findAll()).thenReturn(tweetList);

        mockMvc.perform(get("/tweet/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].tweetText").value("Hello Test"));

        System.out.println("Tüm tweetler başarıyla geldi");
    }

    @DisplayName("User'a ait tüm tweetler geldi mi?")
    @Test
    void findByUserId() throws Exception {
        when(tweetService.findByUserId(user.getId())).thenReturn(tweetList);

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            mockMvc.perform(get("/tweet/user"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[1].tweetText").value("Hello Test2"))
                    .andExpect(jsonPath("$[0].userId").value(1));
        }

        System.out.println("User'a ait tüm tweetler geldi");
    }

    @DisplayName("Id üzerinden tweet getirme")
    @Test
    void findById() throws Exception {
        when(tweetService.findById(1L)).thenReturn(tweet);

        mockMvc.perform(get("/tweet/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tweetText").value("Hello Test"));
    }

    @DisplayName("Var olan tweeti güncelleme testi")
    @Test
    void replaceOrCreate() throws Exception {
        Tweet updatedTweet = new Tweet();
        updatedTweet.setTweetText("Tweet Updated");
        updatedTweet.setLikes(tweetLikes);
        updatedTweet.setUser(user);
        when(tweetService.replaceOrCreate(eq(1L), any(Tweet.class))).thenReturn(updatedTweet);

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            String json = jsonToString(updatedTweet);

            mockMvc.perform(put("/tweet/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.tweetText").value("Tweet Updated"));

            verify(tweetService).replaceOrCreate(eq(1L), any(Tweet.class));
        }

        System.out.println("Var olan tweet güncellendi");

    }

    @DisplayName("Tweet olmadığı için yenisini oluşturma")
    @Test
    void replaceOrCreate_ShouldCreate() throws Exception {
        Tweet newTweet = new Tweet();
        newTweet.setTweetText("New Tweet Created");
        newTweet.setLikes(tweetLikes);
        newTweet.setUser(user);
        when(tweetService.replaceOrCreate(eq(99L), any(Tweet.class))).thenReturn(newTweet);

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            String json = jsonToString(newTweet);

            mockMvc.perform(put("/tweet/99")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.tweetText").value("New Tweet Created"));

            verify(tweetService).replaceOrCreate(eq(99L), any(Tweet.class));
        }

        System.out.println("Tweet üretildi");

    }

    @DisplayName("Tweeti gelen verilere göre güncelleme")
    @Test
    void update() {

    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    //Gönderilen objeyi stringe dönüştürmeye yarıyor
    public static String jsonToString(Object object)
    {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}