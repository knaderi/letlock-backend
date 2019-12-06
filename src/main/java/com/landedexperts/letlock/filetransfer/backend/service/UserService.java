package com.landedexperts.letlock.filetransfer.backend.service;

import java.util.Optional;

import com.landedexperts.letlock.filetransfer.backend.database.jpa.UserDTO;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.types.UserStatusType;

public interface UserService {

    public Optional<UserDTO> findUserByEmailAndStatus(String email, UserStatusType status);

    public Optional<UserDTO> findUserByEmail(String email);

    public Optional<UserDTO> findUserByLoginName(String loginName);

    public Optional<UserDTO> findUserByResetToken(String resetToken);

    public void save(UserDTO user);
}
