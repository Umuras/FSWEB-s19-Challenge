package com.s19Challange.S19_Workintech_TwitterProjectHomework.repository;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Role;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {

        String encodedPassword = passwordEncoder.encode("aA*#4c.");

        Optional<Role> optionalRole = roleRepository.findByAuthority("USER");
        List<Role> roles = new ArrayList<>();


        if(optionalRole.isPresent())
        {
            Role userRole = optionalRole.get();
            roles.add(userRole);
        }else{
            throw new RuntimeException("There is not this role in role table");
        }

        User user = new User();
        user.setEmail("aukUnit2@test.com");
        user.setPassword(encodedPassword);
        user.setFirstName("Ali Test");
        user.setLastName("TEst");
        user.setRoles(roles);

        Optional<User> foundUser = userRepository.findByEmail("aukUnit2@test.com");

        if(foundUser.isPresent())
        {
            System.out.println(foundUser.get());
        }else{
            // Test ortamında save işlemi sırasında User sınıfında @ManyToMany(cascade = CascadeType.ALL) kullanılması
            // nedeniyle "detached entity passed to persist" hatası alınıyordu. Bunun sebebi, Role entity'sinin
            // zaten veritabanında var olan bir kayıt olmasına rağmen, Hibernate'in cascade ile onu sanki yeni bir kayıt
            // gibi persist etmeye çalışmasıdır. Bu durumda Hibernate, daha önce yönetilen (managed) olmayan yani detached
            // bir entity'yi tekrar persist etmeye çalıştığı için hata verir.
            // CascadeType.ALL kaldırılarak, Hibernate'in Role entity'leri üzerinde otomatik persist işlemi yapması engellendi,
            // böylece var olan Role kayıtları tekrar eklenmeye çalışılmadı ve hata çözüldü.
            // detached: EntityManager tarafından artık takip edilmeyen, bağlantısı kopmuş entity.
            // persist: EntityManager’a ilk defa eklenen ve veritabanına kaydedilen yeni entity.
            userRepository.save(user);
        }

    }

    //Her testten sonra çalışacak işlemler için kullanıyoruz.
    @AfterEach
    void tearDown()
    {
        System.out.println("Tüm testler bitti");
    }

    @DisplayName("Can find user by email")
    @Test
    void findByEmail() {
        //Burada veritabanındaki userı bulup onun üzerinden kontrol yapıyoruz.
        Optional<User> foundUser = userRepository.findByEmail("aukUnit2@test.com");
        if(foundUser.isPresent())
        {
            //Null olup olmadığına bakıyoruz, isim ve mail kontrolü yapıyoruz.
            assertNotNull(foundUser.get());
            assertEquals("Ali Test", foundUser.get().getFirstName());
            assertEquals("TEst", foundUser.get().getLastName());
            assertEquals("aukUnit2@test.com",foundUser.get().getEmail());
        }else{
            assertNull(foundUser.get());
        }

    }
}