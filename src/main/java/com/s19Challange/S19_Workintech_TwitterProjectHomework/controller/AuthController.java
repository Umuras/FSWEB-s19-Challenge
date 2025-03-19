package com.s19Challange.S19_Workintech_TwitterProjectHomework.controller;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.LoginResponse;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.RegisterResponse;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.dto.RegistrationUser;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegistrationUser registrationUser)
    {
        User user = authenticationService.register(registrationUser.email(), registrationUser.password(),
                registrationUser.firstName(), registrationUser.lastName());
        return new RegisterResponse(user.getFirstName(), user.getEmail(), "User registration successful");
    }

    @PostMapping("/login")
    public LoginResponse login(@CookieValue(name = "JSESSIONID", required = false) String sessionId, HttpSession session) {
        if(sessionId == null)
        {
            return new LoginResponse("Login successful but sessionId not found!");
        }

        session.setAttribute("sessionId", sessionId);


        return new LoginResponse("Login successful ID:" + sessionId);
    }
}
