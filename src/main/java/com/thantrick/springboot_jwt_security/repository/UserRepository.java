package com.thantrick.springboot_jwt_security.repository;

import com.thantrick.springboot_jwt_security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findUserByEmail(String email);
}
