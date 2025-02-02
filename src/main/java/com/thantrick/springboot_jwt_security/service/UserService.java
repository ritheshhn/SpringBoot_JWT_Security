package com.thantrick.springboot_jwt_security.service;

import com.thantrick.springboot_jwt_security.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto addUser(UserDto user);

    UserDto getUserById(long id);

    UserDto getUserByEmail(String email);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto user);

    void deleteUser(long id);

}
