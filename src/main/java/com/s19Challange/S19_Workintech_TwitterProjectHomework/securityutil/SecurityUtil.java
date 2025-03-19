package com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated())
        {
            User userDetails = (User) authentication.getPrincipal();
            return userDetails.getId();
        }
        throw new RuntimeException("User is not authenticated");
    }

    public static User getCurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated())
        {
            User user = (User) authentication.getPrincipal();
            if(user != null)
            {
                return user;
            }
            throw new RuntimeException("User not found");
        }
        throw new RuntimeException("User is not authenticated");
    }
}
