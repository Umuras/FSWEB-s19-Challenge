package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }


    @Override
    public User findById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent())
        {
            return optionalUser.get();
        }
        throw new TweetException("This user is not present", HttpStatus.NOT_FOUND);
    }
}
