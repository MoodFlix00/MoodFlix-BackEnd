package com.duck.moodflix.users.dto;

import lombok.Data;

@Data
public class DeleteAccountRequest {
    private String password;
}