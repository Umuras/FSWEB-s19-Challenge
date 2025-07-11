package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.*;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.repository.UserRepository;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.JwtUtil;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:3200")
public class AuthController {

//    private AuthenticationService authenticationService;
//    private UserDetailsService userDetailsService;
//    private AuthenticationManager authenticationManager;
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public AuthController(AuthenticationService authenticationService, UserDetailsService userDetailsService,
//                          AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder)
//    {
//        this.authenticationService = authenticationService;
//        this.userDetailsService = userDetailsService;
//        this.authenticationManager = authenticationManager;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @PostMapping("/register")
//    public RegisterResponse register(@RequestBody RegistrationUser registrationUser)
//    {
//        User user = authenticationService.register(registrationUser.email(), registrationUser.password(),
//                registrationUser.firstName(), registrationUser.lastName());
//        return new RegisterResponse(user.getFirstName(), user.getEmail(), "User registration successful");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
//
//        //userDetailsService üzerinden login için gönderilen emailin databasede olup olmadığını kontrol
//        //edip userDetails türünde nesnesini oluşturuyoruz
//        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
//        try {
//            //Frontendden gönderilen email ve passwordun authenticaiton olup olmadığını kontrol ediyoruz.
//            // AuthenticationManager üzerinden kimlik doğrulaması yapıyoruz.
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.email(),
//                            loginRequest.password()
//                    )
//            );
//            //passwordEncoder üzerinden request olarak gönderdiğimiz password ile databasedeki passwordu
//            //karşılaştırıyoruz doğru ise success dönüyoruz. değilse hata fırlatıyoruz.
//            //Buna gerek yok ama bir tür şifre kontrol yöntemi diye bırakıyorum.
//            if(passwordEncoder.matches(loginRequest.password(), userDetails.getPassword()))
//            {
//                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse("Login successful: " + loginRequest.email()));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid credentials"));
//        }
//        return null;
//    }

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails.getUsername());
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        User user = new User();
        if(optionalUser.isPresent())
        {
            user = optionalUser.get();
        }

        return new LoginResponse(new UserResponse(user.getId(), user.getEmail(), user.getFirstName() + " " + user.getLastName()),token);
    }
        @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegistrationUser registrationUser)
    {
        User user = authenticationService.register(registrationUser.email(), registrationUser.password(),
                registrationUser.firstName(), registrationUser.lastName());
        return new RegisterResponse(user.getFirstName(), user.getEmail(), "User registration successful");
    }
}
