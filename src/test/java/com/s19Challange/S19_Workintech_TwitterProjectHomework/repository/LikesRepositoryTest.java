package com.s19Challange.S19_Workintech_TwitterProjectHomework.repository;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Likes;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Role;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Tweet;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikesRepositoryTest {

    private LikesRepository likesRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private TweetRepository tweetRepository;

    @Autowired
    public LikesRepositoryTest(LikesRepository likesRepository, UserRepository userRepository, RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder, TweetRepository tweetRepository)
    {
        this.likesRepository = likesRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tweetRepository = tweetRepository;
    }

    private User user;
    private Tweet tweet;
    boolean hasTweetInDb;

    @BeforeEach
    void setUp() throws Exception {
         user = createUser();
         tweet = createTweet(user);
    }

    private User createUser() throws Exception {
        User newUser = new User();
        newUser.setEmail("aukLikeUnit@test.com");
        newUser.setFirstName("UmurTest");
        newUser.setLastName("KucurTest");
        String encodedPassword = passwordEncoder.encode("1235415");
        newUser.setPassword(encodedPassword);

        Optional<Role> role = roleRepository.findByAuthority("USER");

        //Aşağıdaki ifin kısa hali
        //Eğer role varsa, onu newUser nesnesine addRole ile ekler.
        //Yoksa hiçbir işlem yapılmaz.
        //2. ifPresent ne yapar?
        //    role.ifPresent(...) metodu, eğer role içinde değer varsa o değeri parametre olarak verilen işleve
        //    (lambda veya method reference) gönderir.
        //    Eğer role boşsa (yani içinde değer yoksa), hiçbir şey yapmaz.
        //3. newUser::addRole ne demek?
        //    Bu, method reference kullanımıdır.
        //    newUser nesnesinin addRole(Role role) metodunu işaret eder.
        //    ifPresent içinde, role varsa otomatik olarak newUser.addRole(role) çağrılır.
        //role.ifPresent(newUser::addRole);

        if(role.isPresent())
        {
            newUser.addRole(role.get());
        }else{
            throw new Exception("This role doesn't exist");
        }

       Optional<User> foundUser = userRepository.findByEmail(newUser.getEmail());

        //Eğer foundUser varsa onu dönecek yoksa kaydedip onu dönecek.
        return foundUser.orElseGet(() -> userRepository.save(newUser));

    }

    private Tweet createTweet(User user)
    {

        Tweet newTweet = new Tweet();
        newTweet.setUser(user);
        newTweet.setTweetText("Like için test edilen yeni tweet");
        user.addTweet(newTweet);

        List<Tweet> tweetList = tweetRepository.findByUserId(user.getId());
        tweetList.forEach(tweet1 -> {
            if(tweet1.getTweetText().equals(newTweet.getTweetText()))
            {
                hasTweetInDb = true;
            }
        });

        if(hasTweetInDb)
        {
            return tweetRepository.findByUserId(user.getId()).stream()
                    .filter(t -> t.getTweetText().equals(newTweet.getTweetText()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Tweet bulunamadı"));
        }else {
          return tweetRepository.save(newTweet);
        }

    }

    @AfterEach
    void tearDown() {
        System.out.println("Tüm testler bitti");
    }

    @DisplayName("UserId üzerinden like sayısı bulma")
    @Test
    void findByUserId() {
        Likes like = new Likes();
        like.setTweet(tweet);
        like.setUser(user);
        like.setLikeCreated(true);

        Likes savedLike = likesRepository.save(like);
        user.addLike(savedLike);
        tweet.addLike(savedLike);

        List<Likes> likesList = likesRepository.findByUserId(user.getId());

        assertNotNull(likesList);
        assertEquals(1, likesList.size());

    }

    @DisplayName("Tweet üzerindeki likelar geliyor mu?")
    @Test
    void findByTweetId() {
        Likes like = new Likes();
        like.setTweet(tweet);
        like.setUser(user);
        like.setLikeCreated(true);

        likesRepository.save(like);

        List<Likes> tweetLikes = new ArrayList<>();
        tweetLikes = likesRepository.findByTweetId(tweet.getId());

        assertNotNull(tweetLikes);
        assertEquals(1,tweetLikes.size());
        assertEquals(tweet.getId(), tweetLikes.get(0).getTweet().getId());
        assertEquals(user.getId(), tweetLikes.get(0).getUser().getId());
        assertTrue(tweetLikes.get(0).getLikeCreated());
    }

    @DisplayName("Tweet likes sayısını getiriyor mu?")
    @Test
    void tweetLikesCount() {

        Likes like = new Likes();
        like.setTweet(tweet);
        like.setUser(user);
        like.setLikeCreated(true);

        likesRepository.save(like);

        Long tweetLikesCount = likesRepository.tweetLikesCount(tweet.getId());

        assertEquals(1L, tweetLikesCount);
    }
}