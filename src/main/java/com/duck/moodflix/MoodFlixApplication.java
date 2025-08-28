package com.duck.moodflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") //
@SpringBootApplication
public class MoodFlixApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoodFlixApplication.class, args);
    }

}
