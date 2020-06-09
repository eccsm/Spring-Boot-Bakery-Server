package com.eccsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eccsm.model.PasswordResetToken;

@Repository
public interface IPasswordResetToken extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

}
