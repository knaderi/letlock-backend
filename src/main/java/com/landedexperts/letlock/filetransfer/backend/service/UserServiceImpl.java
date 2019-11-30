package com.landedexperts.letlock.filetransfer.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.database.jpa.UserDTO;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.repository.UserRepository;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.types.UserStatusType;



@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<UserDTO> findUserByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(user);
    }

    @Override
    public Optional<UserDTO> findUserByEmailAndStatus(String email, UserStatusType status) {
        return userRepository.findUserByEmailAndStatus(email, status);
    }
}