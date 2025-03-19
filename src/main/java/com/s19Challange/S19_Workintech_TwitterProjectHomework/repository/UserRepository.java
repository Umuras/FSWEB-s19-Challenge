package com.s19Challange.S19_Workintech_TwitterProjectHomework.repository;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);
}
