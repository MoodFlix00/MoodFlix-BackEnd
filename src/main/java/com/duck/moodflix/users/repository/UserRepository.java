package com.duck.moodflix.users.repository;

import com.duck.moodflix.users.domain.entity.User;
import com.duck.moodflix.users.domain.entity.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * [추가] 활성(ACTIVE) 상태인 사용자만 이메일로 조회합니다.
     * @param email 조회할 이메일
     * @param status UserStatus.ACTIVE 고정 사용
     * @return Optional<User> 활성 사용자
     */
    Optional<User> findByEmailAndStatus(String email, UserStatus status);

}