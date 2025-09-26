package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;


    private UserService userService;

    private User user;
    private User foundUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("ust@test.com");

        userService = new UserServiceImpl(userRepository);
    }

    @DisplayName("Id ile User bulma")
    @Test
    void findById() {
        //Arrange
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        //Act
        foundUser = userService.findById(1L);


        //Assert
        assertNotNull(foundUser);
        assertEquals("Test", foundUser.getFirstName());
        assertEquals("User", foundUser.getLastName());
        assertEquals("ust@test.com", foundUser.getEmail());
        verify(userRepository).findById(eq(1L));
    }

    @DisplayName("İlgili Id de User bulunamama durumu")
    @Test
    void cantFindById(){
        //Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> userService.findById(2L)).isInstanceOf(TweetException.class)
                .hasMessageContaining("This user is not present");

        verify(userRepository).findById(2L);
    }
}