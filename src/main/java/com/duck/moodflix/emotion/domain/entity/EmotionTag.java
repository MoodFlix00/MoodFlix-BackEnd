package com.duck.moodflix.emotion.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "emotion_tags",
        uniqueConstraints = @UniqueConstraint(columnNames = "tagName")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmotionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String tagName;
}