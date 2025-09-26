package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.LikesResponse;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.JwtUtil;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.LikesService;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.TweetService;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.UserService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//WebMvcTest bize bir Entegrasyon testi yazacağımızı gösteriyor
@WebMvcTest(LikesController.class)

//Eğer testlerinde güvenlik, authentication, authorization gibi konuları test etmek istemiyorsan,
//Ya da filtrelerin testini ayrı yapıp controller’ı “filtrelerden bağımsız” test etmek istiyorsan,
//O zaman @AutoConfigureMockMvc(addFilters = false) ile filtreleri kapatabilirsin.
@AutoConfigureMockMvc(addFilters = false)
class LikesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //Entegrasyon testlerine özel bir şeydir. Bu sayede bu obje gerçekten hiçbir şekilde çalışmayacak
    //Bu obje Mocklanıcaktır demek. Bu objenin üzerinde Mocking yapılacaktır demek. Tüm @MockitoBeanler
    //JwtUtil'i MockitoBean yapmadığın zaman testler çalışmıyor.
    @MockitoBean
    private JwtUtil jwtUtils;

    @MockitoBean
    private LikesService likesService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TweetService tweetService;

    private Likes like;
    private Tweet tweet;
    private User user;
    private LikesResponse likesResponse;
    private List<Likes> likesList;


    @BeforeEach
    void setup(){
        user = new User();
        user.setFirstName("Test");
        user.setLastName("LikeTest");
        user.setId(1L);
        user.setEmail("auktTest@test.com");

        tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser(user);
        tweet.setTweetText("Test Tweet");

        likesList = new ArrayList<>();
        like = new Likes();
        like.setId(1L);
        like.setLikeCreated(true);
        like.setUser(user);
        likesList.add(like);
        tweet.setLikes(likesList);
        like.setTweet(tweet);
        user.addLike(like);

        likesResponse =
                new LikesResponse(like.getId(),like.getLikeCreated(), like.getUser().getId(),
                        like.getUser().getUsername(), like.getUser().getEmail(), like.getTweet().getId(),
                        like.getTweet().getTweetText(),like.getTweet().getUser().getUsername(), like.getTweet().getUser().getEmail());

    }

    @DisplayName("Id üzerinde like bulma")
    @Test
        //throws Exception yazmamızın sebebi mockMvc.performun exception fırlatma olasılığı olduğu için bunu yazmak
        // zorundayız yoksa hata veriyor.
    void findById() throws Exception{
        //Burada şunu yapıyoruz, likesService'i mocklamıştık yani gerçekten çalışmayacak çalışıp da bize bir değer
        //dönüyormuş gibi yapacak.
        //Burada ise likesService.findById(1L) çalıştğında BeforeEachde oluşturduğumuz like nesnesi dönsün
        //istiyoruz.
        when(likesService.findById(1L)).thenReturn(like);

        //Burada da /tweet/like/{id} endpointe request attığımızda dönecek olayı kontrol ediyoruz.
        mockMvc.perform(get("/tweet/like/1"))
                //Http status codeunun OK yani 200 geleceğini söylüyoruz
                .andExpect(status().isOk())
                //İçeriğin Json türünde geleceğini söylüyoruz
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //Gelen cevap içindeki json verisinin bu değere eşit olacağını söylüyoruz.
                .andExpect(jsonPath("$.likeId").value(1L))
                .andExpect(jsonPath("$.likeCreated").value(true))
                .andExpect(jsonPath("$.likeUserName").value("Test LikeTest"));

        //likeService.findById methodunun çalıştığını kontrol ediyoruz.
        verify(likesService).findById(like.getId());
    }

    @DisplayName("User id üzerinden like bulma")
    @Test
    void findByUserId() throws Exception {
        when(likesService.findByUserId()).thenReturn(likesList);

        mockMvc.perform(get("/tweet/likes/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].likeId").value(1L))
                .andExpect(jsonPath("$[0].likeCreated").value(true))
                .andExpect(jsonPath("$[0].likeUserName").value("Test LikeTest"))
                .andExpect(jsonPath("$[0].tweetId").value(1L))
                .andExpect(jsonPath("$[0].tweetText").value("Test Tweet"));

        verify(likesService).findByUserId();
    }

    @DisplayName("Tweet idsi üzerinden o tweeti ait likeları bulma")
    @Test
    void findByTweetId() throws Exception{
        when(likesService.findByTweetId(1L)).thenReturn(likesList);

        mockMvc.perform(get("/tweet/likes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].likeId").value(1L))
                .andExpect(jsonPath("$[0].likeCreated").value(true))
                .andExpect(jsonPath("$[0].tweetId").value(1L))
                .andExpect(jsonPath("$[0].tweetText").value("Test Tweet"));

        verify(likesService).findByTweetId(like.getTweet().getId());
    }

    @DisplayName("Like kaydediliyor mu?")
    @Test
    void save() throws Exception{

        //tweetService için findById methodu çalıştığında 1L gönderildiğinde tweet nesnesi dönsün gibi davranmasını
        //bekliyoruz.
        when(tweetService.findById(1L)).thenReturn(tweet);
        //userService.findById 1L için çalıştığında user nesnesi dönsün gibi davranmasını bekliyoruz.
        when(userService.findById(eq(1L))).thenReturn(user);
        //likesService.save metodu 1L ve herhangi bir like nesnesi için çalıştırıldığında bizim oluşturduğumuz like
        //nesnesinin dönmesini bekliyoruz.
        when(likesService.save(eq(1L), any(Likes.class))).thenReturn(like);

        //SecurityUtil sınıfındaki method static olduğu için bu şekilde MockedStatic kullanmak
        //zorundayız. Bunu yazmamızın sebebi controllerdaki save metodu içinde bu kullanıldığı için.
        try(MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {

            //mockedStatic üzerinden SecurityUtil::getCurrentUserId'nin 1L dönmesini istiyoruz
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            //Controller post işlemi yaptığımız için post olarak yazıyoruz.
            mockMvc.perform(post("/tweet/like/1")
                            //requestBodynin türünü ve değerini yazıyoruz.
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonToString(like))
                            .accept(MediaType.APPLICATION_JSON))
                    //Like başarılı şekilde kaydedildiğinde HTTP 201 (Created) döner, bu yüzden status().isCreated() beklenir.
                    //içeriğin json ve json üzerinden değer kontrolü yapıyoruz.
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.likeId").value(1L))
                    .andExpect(jsonPath("$.tweetId").value(1L))
                    .andExpect(jsonPath("$.tweetText").value("Test Tweet"));

            //likesService'in userService'in ve tweetService'in verilen değerler doğrultusunda düzgün
            //çalışıp çalışmadığının kontrolünü yapıyoruz.
            verify(likesService).save(eq(1L), any(Likes.class));
            verify(userService).findById(1L);
            verify(tweetService).findById(1L);
        }
    }

    @DisplayName("Tweet üzerindeki like kaldırılıyor mu?")
    @Test
    void delete() throws Exception{

        when(likesService.findById(eq(1L))).thenReturn(like);

        mockMvc.perform(post("/tweet/dislike/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.likeId").value(1L))
                .andExpect(jsonPath("$.likeUserId").value(1L))
                .andExpect(jsonPath("$.likeCreated").value(true))
                .andExpect(jsonPath("$.likeUserName").value("Test LikeTest"))
                .andExpect(jsonPath("$.likeUserEmail").value("auktTest@test.com"))
                .andExpect(jsonPath("$.tweetId").value(1L))
                .andExpect(jsonPath("$.tweetText").value("Test Tweet"))
                .andExpect(jsonPath("$.tweetUserName").value("Test LikeTest"))
                .andExpect(jsonPath("$.tweetUserEmail").value("auktTest@test.com"));


        verify(likesService).delete(eq(1L));
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