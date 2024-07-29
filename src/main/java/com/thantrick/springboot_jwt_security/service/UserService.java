package com.thantrick.springboot_jwt_security.service;

import com.thantrick.springboot_jwt_security.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto user);

    UserDto getUserById(long id);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto user);

    void deleteUser(long id);

}
