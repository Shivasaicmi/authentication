package com.shivasai.authentication.Entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Repository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);

}
