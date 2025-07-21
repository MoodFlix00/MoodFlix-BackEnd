package com.duck.moodflix.users.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emotion_tags")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmotionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String tagName;
}