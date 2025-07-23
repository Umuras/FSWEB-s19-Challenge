package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.TweetRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.UserRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TweetServiceImplTest {

    private TweetService tweetService;

    @Mock
    private UserService userService;

    @Mock
    private TweetRepository tweetRepository;

    private User user;
    private Tweet tweet;


    @BeforeEach
    void setUp() {
        tweetService = new TweetServiceImpl(tweetRepository, userService);
        user = new User();
        user.setFirstName("ali");
        user.setId(1L);

        tweet = new Tweet();
        tweet.setId(1L);
        tweet.setTweetText("Test tweet");
        tweet.setUser(user);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Tüm testler bitti");
    }

    @DisplayName("Can find All Tweet")
    @Test
    void findAll() {
        tweetService.findAll();
        verify(tweetRepository).findAll();
    }

    @DisplayName("Can find all tweets by userId")
    @Test
    void findByUserId() {
//        tweetService.findByUserId(1L);
//        verify(tweetRepository).findByUserId(1L);

        // Arrange
        when(tweetRepository.findByUserId(1L)).thenReturn(List.of(tweet));

        // Act
        List<Tweet> tweets = tweetService.findByUserId(1L);

        // Assert
        assertFalse(tweets.isEmpty());
        assertEquals("Test tweet", tweets.get(0).getTweetText());
        verify(tweetRepository).findByUserId(1L);
    }

    @DisplayName("Can find a tweet by id")
    @Test
    void findById() {
        //Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        //Act
        Tweet foundedTweet = tweetService.findById(1L);

        // Assert
        assertNotNull(tweet);
        assertEquals("Test tweet", tweet.getTweetText());
        verify(tweetRepository).findById(1L);
    }

    @DisplayName("Can replace or create a tweet")
    @Test
    void replaceOrCreate() {
        // Test için örnek bir User nesnesi oluşturuyoruz
        User user = new User();
        user.setFirstName("ali");

        // Test için örnek bir Tweet nesnesi oluşturuyoruz
        Tweet tweet = new Tweet();
        tweet.setTweetText("Tweet created."); // tweet içeriği

        // SecurityUtil.getCurrentUserId() static olduğu için mock'lamak gerekiyor
        // try-with-resources bloğu içinde static mock açılır ve test bitince kapanır
        try(MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)){
            // SecurityUtil.getCurrentUserId() çağrıldığında 1L dönmesini sağlıyoruz
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            // userService.findById(1L) çağrıldığında yukarıda tanımladığımız kullanıcıyı döndürmesini sağlıyoruz
            when(userService.findById(1L)).thenReturn(user);

            // tweetRepository.findById(1L) boş dönüyor → bu sayede "yeni tweet oluşturulacak" senaryosu test ediliyor
            when(tweetRepository.findById(1L)).thenReturn(Optional.empty());

            // tweetRepository.save(...) çağrıldığında, gelen tweet objesini döndürsün diyoruz
            when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);

            //Act
            // Servis methodunu çağırıyoruz: yeni tweet oluşturulacak
            Tweet replaceOrCreateTweet = tweetService.replaceOrCreate(1L,tweet);

            //Assert
            // Sonucun null olmadığını test ediyoruz
            assertNotNull(replaceOrCreateTweet);
            // Tweet içeriğinin doğru şekilde güncellenip güncellenmediğini kontrol ediyoruz
            assertEquals("Tweet created.", replaceOrCreateTweet.getTweetText());

            // userService.findById(1L) çağrıldı mı kontrol ediyoruz
            verify(userService).findById(1L);
            // tweetRepository.save(tweet) çağrıldı mı kontrol ediyoruz
            verify(tweetRepository).save(tweet);
        }
    }

    @DisplayName("Can update a tweet")
    @Test
    void update() {
        // Arrange - Test için gerekli veriler hazırlanır

        Tweet existingTweet = new Tweet(); // Veritabanında var olan tweet nesnesi simülasyonu
        existingTweet.setId(1L); // Tweet'in ID'si 1 olarak ayarlanıyor

        User existingUser = new User(); // Tweet sahibi kullanıcı nesnesi oluşturuluyor
        existingUser.setId(1L); // Kullanıcının ID'si 1 olarak ayarlanıyor
        existingTweet.setUser(existingUser); // Tweetin sahibi atanıyor

        existingTweet.setTweetText("Original tweet"); // Mevcut tweetin metni

        Tweet updatedTweet = new Tweet(); // Güncelleme için gönderilen yeni tweet nesnesi
        updatedTweet.setTweetText("Updated tweet"); // Yeni tweet metni
        updatedTweet.setUser(existingUser); // Aynı kullanıcı ile güncelleme yapılıyor


        // Mocklama işlemleri
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(existingTweet));
        // TweetRepository.findById çağrıldığında mevcut tweet dönsün

       // when(tweetRepository.save(any(Tweet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // TweetRepository.save çağrıldığında kaydedilen tweet nesnesini döndürsün

        // SecurityUtil static metodunu mocklama
        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
            // SecurityUtil.getCurrentUserId() çağrıldığında 1 dönsün (kullanıcı ID)

            // Act - Test edilecek metot çağrılır
            Tweet result = tweetService.update(1L, updatedTweet);
            // TweetService'deki update metodu çalıştırılır, sonucu result'a atanır

            // Assert - Sonuçlar doğrulanır
            assertNotNull(result); // Güncellenen tweet null olmamalı
            assertEquals("Updated tweet", result.getTweetText()); // Metin güncellenmiş mi?
            //assertEquals(10, result.getLikes()); // Beğeni sayısı güncellenmiş mi?
            assertEquals(existingUser, result.getUser()); // Kullanıcı değişmemiş mi?
            assertEquals(1L, result.getId()); // Tweet ID aynı mı?

            // Mocklanan metodların çağrılıp çağrılmadığını doğrula
            verify(tweetRepository).findById(1L); // findById çağrılmış mı?
            //verify(tweetRepository).save(result); // save çağrılmış mı?
        }
    }

    @DisplayName("Can save a tweet")
    @Test
    void canSave() {
        //Arrange
        Tweet tweet2 = new Tweet();
        tweet2.setTweetText("Bu benim save testi tweetim");
        tweet2.setUser(user);


        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet2);
        when(userService.findById(1L)).thenReturn(user);

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
            //Act
            Tweet savedTweet = tweetService.save(tweet2);

            //Assert
            assertNotNull(savedTweet);
            assertEquals("Bu benim save testi tweetim", savedTweet.getTweetText());
            assertEquals(1L,savedTweet.getUser().getId());

            verify(tweetRepository).save(savedTweet);
        }

    }

    @DisplayName("Can delete a tweet")
    @Test
    void delete() {

        //Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            //Act
            Tweet reachedTweet = tweetService.findById(1L);
            tweetService.delete(reachedTweet.getId());

            //Assert
            assertNotNull(reachedTweet);


            verify(tweetRepository).delete(reachedTweet);
        }

    }
}