package com.landedexperts.letlock.filetransfer.backend.database.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.landedexperts.letlock.filetransfer.backend.database.jpa.UserDTO;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.types.UserStatusType;


public interface UserRepository extends JpaRepository<UserDTO, Long> {

    Optional<UserDTO> findUserByEmailAndStatus(String email, UserStatusType status);
    Optional<UserDTO> findUserByLoginName(String loginName);
    Optional<UserDTO> findUserByEmail(String email);
    Optional<UserDTO> findByResetToken(String resetToken);
}
