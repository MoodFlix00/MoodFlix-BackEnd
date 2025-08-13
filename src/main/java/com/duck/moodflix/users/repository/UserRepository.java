package com.duck.moodflix.users.repository;

import com.duck.moodflix.users.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일을 기준으로 사용자를 조회합니다.
     * @param email 사용자의 이메일 주소
     * @return Optional<User> 조회된 사용자. 존재하지 않을 경우 Optional.empty() 반환
     */
    Optional<User> findByEmail(String email);

}