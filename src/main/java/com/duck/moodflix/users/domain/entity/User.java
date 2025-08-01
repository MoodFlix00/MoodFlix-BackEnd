package com.duck.moodflix.users.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String email;

    // 자체회원가입이면 hash된 패스워드, 소셜계정은 null
    @Column(length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 20)
    private String gender; // "M", "F"

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(length = 255)
    private String profileImage;

    // "local" | "kakao" | "google" 등 (소셜/로컬 구분)
    @Column(nullable = false, length = 20)
    private String provider;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    // soft delete: 탈퇴 플래그 (필요시)
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    // JPA Auditing 등으로 자동화
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.deleted = false;
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}