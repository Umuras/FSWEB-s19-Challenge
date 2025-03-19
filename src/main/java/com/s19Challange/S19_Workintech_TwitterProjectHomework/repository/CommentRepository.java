package com.s19Challange.S19_Workintech_TwitterProjectHomework.repository;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
