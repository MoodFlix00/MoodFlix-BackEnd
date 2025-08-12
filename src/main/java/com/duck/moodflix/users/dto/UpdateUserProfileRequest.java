package com.duck.moodflix.users.dto;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String name;           // 필수 (이름)
    private String birthDate;      // ISO String "YYYY-MM-DD"
    private String gender;         // "M" | "F" | null
    private String profileImage;   // URL
}