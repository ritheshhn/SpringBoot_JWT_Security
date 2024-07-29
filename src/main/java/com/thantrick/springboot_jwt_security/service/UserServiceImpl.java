package com.thantrick.springboot_jwt_security.service;

import com.thantrick.springboot_jwt_security.dto.UserDto;
import com.thantrick.springboot_jwt_security.entity.UserEntity;
import com.thantrick.springboot_jwt_security.repository.UserRepository;
import com.thantrick.springboot_jwt_security.utils.GeneralUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralUtils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto addUser(UserDto user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        UserEntity userByEmail = userRepository.findUserByEmail(user.getEmail());

        if(userByEmail != null){
            throw new RuntimeException("EmailId already registered. Please try login or use another Email Id");
        }

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(utils.generateUserId(25));

        UserEntity savedUser = userRepository.save(userEntity);

        BeanUtils.copyProperties(savedUser, user);
        return user;
    }

    @Override
    public UserDto getUserById(long id) {
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRepository.findUserByEmail(email), userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto user) {
        return null;
    }

    @Override
    public void deleteUser(long id) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userByEmail = userRepository.findUserByEmail(username);

        if (userByEmail == null) throw new UsernameNotFoundException("User not registered Yet... Please Register first to login");

        return new User(userByEmail.getEmail(), userByEmail.getEncryptedPassword(), new ArrayList<>());
    }
}
