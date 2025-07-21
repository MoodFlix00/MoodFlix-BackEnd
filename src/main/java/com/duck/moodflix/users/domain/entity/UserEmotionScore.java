package com.duck.moodflix.users.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_emotion_scores")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmotionScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private EmotionTag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_input_id", nullable = false)
    private UserEmotionInput emotionInput;

    private Float score;
}

