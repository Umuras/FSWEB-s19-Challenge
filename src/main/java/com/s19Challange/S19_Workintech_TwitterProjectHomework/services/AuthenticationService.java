package com.s19Challange.S19_Workintech_TwitterProjectHomework.services;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Role;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.exception.TweetException;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.RoleRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password, String name, String surname)
    {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent())
        {
            throw new RuntimeException("User with given email already exist");
        }

        String encodedPassword = passwordEncoder.encode(password);

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
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setFirstName(name);
        user.setLastName(surname);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}
