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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//WebMvcTest bize bir Entegrasyon testi yazacağımızı gösteriyor
@WebMvcTest(TweetController.class)
//Eğer testlerinde güvenlik, authentication, authorization gibi konuları test etmek istemiyorsan,
//Ya da filtrelerin testini ayrı yapıp controller’ı “filtrelerden bağımsız” test etmek istiyorsan,
//O zaman @AutoConfigureMockMvc(addFilters = false) ile filtreleri kapatabilirsin.
@AutoConfigureMockMvc(addFilters = false)
class TweetControllerTest {

    //Burada proje içinde kullanacağımız mockMvc nesnesini oluştuyoruz.
    //Field Injection yapıyoruz.
    @Autowired
    private MockMvc mockMvc;

    //Entegrasyon testlerine özel bir şeydir. Bu sayede bu obje gerçekten hiçbir şekilde çalışmayacak
    //Bu obje Mocklanıcaktır demek. Bu objenin üzerinde Mocking yapılacaktır demek. Tüm @MockitoBeanler
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

    //Controllerdaki methodları test ederken kullanacağımız fieldlar.
    private Tweet tweet;
    private Tweet tweet2;
    private User user;
    private List<Tweet> tweetList;
    private List<Likes> tweetLikes;

    //Testler başlamadan önce ilk burası çalışıp, gerekli nesnelerin oluşturulup değer ataması yapılıp,
    //test içinde kullanılır hale getiriliyor.
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
        user.setEmail("auk@test.com");
        user.addTweet(tweet);
        user.addTweet(tweet2);
        tweet.setUser(user);
        tweet2.setUser(user);
        tweet.setLikes(tweetLikes);
        tweet2.setLikes(tweetLikes);
    }


    @DisplayName("Tüm tweetler geldi mi?")
    @Test
    //throws Exception yazmamızın sebebi mockMvc.performun exception fırlatma olasılığı olduğu için bunu yazmak
    //zorundayız yoksa hata veriyor.
    void findAll() throws Exception {
        //Burada şunu yapıyoruz, tweetService'i mocklamıştık yani gerçekten çalışmayacak çalışıp da bize bir değer
        //dönüyormuş gibi yapacak.
        //Burada ise tweetService.findAll() çalıştğında BeforeEachde içini doldurduğumuz tweetListesini dönsün
        //istiyoruz.
        when(tweetService.findAll()).thenReturn(tweetList);

        //Burada da /tweet/all endpointe request attığımızda dönecek olayı kontrol ediyoruz.
        mockMvc.perform(get("/tweet/all"))
                //Http status codeunun OK yani 200 geleceğini söylüyoruz
                .andExpect(status().isOk())
                //İçeriğin Json türünde geleceğini söylüyoruz
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //Gelen cevap içindeki json verisinin bu değere eşit olacağını söylüyoruz.
                .andExpect(jsonPath("$[0].tweetText").value("Hello Test"));

        //tweetService.findAll methodunun çalıştığını kontrol ediyoruz.
        verify(tweetService).findAll();
        
    }

    @DisplayName("User'a ait tüm tweetler geldi mi?")
    @Test
    void findByUserId() throws Exception {
        //tweetService.findByUserId çalıştığında bizim BeforeEachte oluşturduğumuz userın idsi
        //Kendi oluşturduğumuz tweetList dönsün istiyoruz.
        when(tweetService.findByUserId(user.getId())).thenReturn(tweetList);

        //SecurityUtil sınıfındaki method static olduğu için bu şekilde MockedStatic kullanmak
        //zorundayız.
        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            //mockedStatic üzerinden SecurityUtil::getCurrentUserId'nin 1L dönmesini istiyoruz
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            //tweet/user'a request attığımızda olacakları test ediyoruz.
            mockMvc.perform(get("/tweet/user"))
                    //Http statusun OK yani 200 gelmesini bekliyoruz.
                    .andExpect(status().isOk())
                    //Iceriğin Json olmasını bekleniyor
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    //Gelen değerlerin json formatında kontrolü yapılıyor.
                    .andExpect(jsonPath("$[1].tweetText").value("Hello Test2"))
                    .andExpect(jsonPath("$[0].userId").value(1));
        }

        //tweetService.findByUserId'nin kullanıldığı test ediliyor.
        verify(tweetService).findByUserId(user.getId());
    }

    @DisplayName("Id üzerinden tweet getirme")
    @Test
    void findById() throws Exception {
        //tweetService.findById 1L gönderildiğinde tweet nesnesinin dönmesi bekleniyor
        when(tweetService.findById(1L)).thenReturn(tweet);

        //tweet/1 ' e get requesti atınca status codeun 200, türün json ve veri kontrolü yapılıyor
        mockMvc.perform(get("/tweet/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tweetText").value("Hello Test"));

        //tweetService.findById'nin bu method içinde çalıştırılması kontrolü yapılıyor
        verify(tweetService).findById(1L);
    }

    @DisplayName("Var olan tweeti güncelleme testi")
    @Test
    void replaceOrCreate() throws Exception {
        //Method içinde kullanacağımız yeni tweet objesini oluşturuyoruz.Burada var olan tweeti güncelliyoruz.
        Tweet updatedTweet = new Tweet();
        updatedTweet.setTweetText("Tweet Updated");
        updatedTweet.setLikes(tweetLikes);
        updatedTweet.setUser(user);
        //tweetService.replaceOrCreate methodu çalıştığında updatedTweet döndürmesini istiyoruz.
        //Argüman olarak eq(1L) diyerek idnin 1L olma şartını, tweet olarak herhangi bir tweet nesnesi
        //gönderebileceğini söylüyoruz.
        when(tweetService.replaceOrCreate(eq(1L), any(Tweet.class))).thenReturn(updatedTweet);

        //SecurityUtil sınıfındaki method static olduğu için bu şekilde MockedStatic kullanmak
        //zorundayız.
        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            //mockedStatic üzerinden SecurityUtil::getCurrentUserId'nin 1L dönmesini istiyoruz
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            //updatedTweet nesnesini json formatına dönüştüyoruz, çünkü istek atarken içerik tipinin
            //json formatında olacağını söylüyoruz ve o şekilde kabul edileceğini söylüyoruz.
            String json = jsonToString(updatedTweet);

            //Controller put işlemi yaptığımız için put olarak yazıyoruz.
            mockMvc.perform(put("/tweet/1")
                    //requestBodynin türünü ve değerini yazıyoruz.
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    //statusun 200, içeriğin json ve json üzerinden değer kontrolü yapıyoruz.
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.tweetText").value("Tweet Updated"));

            //Bu methodun çalıştığını doğruluyoruz.
            verify(tweetService).replaceOrCreate(eq(1L), any(Tweet.class));
        }

    }

    @DisplayName("Tweet olmadığı için yenisini oluşturma")
    @Test
    void replaceOrCreate_ShouldCreate() throws Exception {
        //Method içinde kullanacağımız yeni tweet objesini oluşturuyoruz.Burada yeni tweet oluşturuyoruz.
        Tweet newTweet = new Tweet();
        newTweet.setTweetText("New Tweet Created");
        newTweet.setLikes(tweetLikes);
        newTweet.setUser(user);
        //tweetService.replaceOrCreate'i çalıştırdığımızda id değeri 99L olan herhangi bir tweet nesnesi gönderilip
        //sonucunun bize newTweet olarak geleceğini söylüyoruz.
        when(tweetService.replaceOrCreate(eq(99L), any(Tweet.class))).thenReturn(newTweet);

        //SecurityUtil sınıfındaki method static olduğu için bu şekilde MockedStatic kullanmak
        //zorundayız.
        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            //mockedStatic üzerinden SecurityUtil::getCurrentUserId'nin 1L dönmesini istiyoruz
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            //newTweet nesnesini json formatına dönüştüyoruz, çünkü istek atarken içerik tipinin
            //json formatında olacağını söylüyoruz ve o şekilde kabul edileceğini söylüyoruz.
            String json = jsonToString(newTweet);

            //Controller put işlemi yaptığımız için put olarak yazıyoruz.
            mockMvc.perform(put("/tweet/99")
                            //requestBodynin türünü ve değerini yazıyoruz.
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    //statusun 200, içeriğin json ve json üzerinden değer kontrolü yapıyoruz.
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.tweetText").value("New Tweet Created"));

            //tweetService.replaceOrCreate in çalıştığının kontrolünü yapıyoruz.
            verify(tweetService).replaceOrCreate(eq(99L), any(Tweet.class));
        }

    }

    @DisplayName("Tweeti gelen verilere göre güncelleme")
    @Test
    void update() throws Exception {
        //Methodun bize dönmesini istediğimiz yeni tweet objesini oluşturuyoruz.
        Tweet newTweet = new Tweet();
        newTweet.setUser(tweet.getUser());
        newTweet.setLikes(tweetLikes);
        newTweet.setTweetText("Tweet updated.");

        //tweetService.update çalıştığında eq(1L) idsi 1L olma şartı ve tweet nesnesinin de
        //herhangi bir nesne olduğunu söylüyoruz ve dönen değer olarak da newTweet diyoruz.
        when(tweetService.update(eq(1L),any(Tweet.class))).thenReturn(newTweet);

        //SecurityUtil sınıfındaki method static olduğu için bu şekilde MockedStatic kullanmak
        //zorundayız. Bunu yazmamızın sebebi controllerdaki update metodu içinde bu kullanıldığı için.
        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            //mockedStatic üzerinden SecurityUtil::getCurrentUserId'nin 1L dönmesini istiyoruz
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            //newTweet nesnesini json formatına dönüştüyoruz, çünkü istek atarken içerik tipinin
            //json formatında olacağını söylüyoruz ve o şekilde kabul edileceğini söylüyoruz.
            String json = jsonToString(newTweet);

            //Controller patch işlemi yaptığımız için patch olarak yazıyoruz.
            mockMvc.perform(patch("/tweet/1")
                     //requestBodynin türünü ve değerini yazıyoruz.
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
                    //statusun 200, içeriğin json ve json üzerinden değer kontrolü yapıyoruz.
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.tweetText").value("Tweet updated."));

            //tweetService.update in çalıştığının kontrolünü yapıyoruz.
            verify(tweetService).update(eq(1L),any(Tweet.class));
        }

    }

    @DisplayName("Tweet save ediliyor mu?")
    @Test
    void save() throws Exception {
        //Methodun bize dönmesini istediğimiz yeni tweet objesini oluşturuyoruz.
        Tweet newTweet = new Tweet();
        newTweet.setTweetText("Kaydedilecek tweetim");
        newTweet.setLikes(tweetLikes);
        newTweet.setUser(user);

        //Herhangi bir tweet nesnesi tweetService.save'e argüman olarak gönderildiğinde
        //newTweet nesnesini dönecekmiş gibi davranmasını bekliyoruz.
        when(tweetService.save(any(Tweet.class))).thenReturn(newTweet);
        //userService.findById metodu kullanıldığı gönderilen argüman 1L olmak zorunda user nesnesinin dönmesini
        //bekliyoruz. Gerçekte çalışmıyor, çalışıyormuş gibi yapıyoruz.
        when(userService.findById(eq(1L))).thenReturn(user);

        //SecurityUtil sınıfındaki method static olduğu için bu şekilde MockedStatic kullanmak
        //zorundayız. Bunu yazmamızın sebebi controllerdaki update metodu içinde bu kullanıldığı için.
        try(MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class))
        {
            //mockedStatic üzerinden SecurityUtil::getCurrentUserId'nin 1L dönmesini istiyoruz
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            //Controller post işlemi yaptığımız için post olarak yazıyoruz.
            mockMvc.perform(post("/tweet")
                    //requestBodynin türünü ve değerini yazıyoruz.
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonToString(newTweet))
                    .accept(MediaType.APPLICATION_JSON))
                    //Tweet başarılı şekilde kaydedildiğinde HTTP 201 (Created) döner, bu yüzden status().isCreated() beklenir.
                    //içeriğin json ve json üzerinden değer kontrolü yapıyoruz.
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.tweetText").value("Kaydedilecek tweetim"));

            //tweetService.save in çalıştığının kontrolünü yapıyoruz.
            verify(tweetService).save(any(Tweet.class));
            //userService.findById'nin 1L olarak çalıştığının kontrolünü yapıyoruz.
            verify(userService).findById(eq(1L));
        }
    }

    @DisplayName("Tweet siliniyor mu?")
    @Test
    void canDelete() throws Exception {
        //tweetService.findById metoduna argüman olarak 1L gönderilme şartı ekliyoruz ve dönen
        //nesne olarak tweet nesnesinin dönmesini bekliyoruz.
        when(tweetService.findById(eq(1L))).thenReturn(tweet);

        //SecurityUtil sınıfındaki method static olduğu için bu şekilde MockedStatic kullanmak
        //zorundayız. Bunu yazmamızın sebebi controllerdaki update metodu içinde bu kullanıldığı için.
        try(MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            //mockedStatic üzerinden SecurityUtil::getCurrentUserId'nin 1L dönmesini istiyoruz
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            //Controller delete işlemi yaptığımız için delete olarak yazıyoruz.
            mockMvc.perform(delete("/tweet/1"))
                    //delete yapıldığında herhangi bir dönen değer veya requestbody olmadığı için bu şekilde
                    //yazıyoruz. Sadece status OK yani 200 mü diye kontrol ediyoruz.
                    .andExpect(status().isOk());

            //tweetService.delete() argüman olarak 1L alıp çalıştığını kontrol ediyoruz.
            verify(tweetService).delete(eq(1L));
        }
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