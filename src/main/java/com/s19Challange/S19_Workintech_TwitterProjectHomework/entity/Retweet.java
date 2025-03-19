package com.s19Challange.S19_Workintech_TwitterProjectHomework.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "retweet", schema = "s19")
public class Retweet {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
}
