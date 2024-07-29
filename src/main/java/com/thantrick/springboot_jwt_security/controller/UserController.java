package com.thantrick.springboot_jwt_security.controller;

import com.thantrick.springboot_jwt_security.dto.UserDto;
import com.thantrick.springboot_jwt_security.model.UserDetailsRequestModel;
import com.thantrick.springboot_jwt_security.model.UserResponseModel;
import com.thantrick.springboot_jwt_security.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody UserDetailsRequestModel userDetails){
        UserDto user = new UserDto();
        BeanUtils.copyProperties(userDetails, user);

        UserDto createdUser = userService.addUser(user);

        UserResponseModel userResponse = new UserResponseModel();
        BeanUtils.copyProperties(createdUser, userResponse);

        return new ResponseEntity<UserResponseModel>(userResponse,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public UserDetailsRequestModel getUserById(@PathVariable long id){
        userService.getUserById(id);
        return null;
    }

    @GetMapping
    public List<UserDetailsRequestModel> getAllUsers(){
        userService.getAllUsers();
        return null;
    }

    @PutMapping
    public UserDetailsRequestModel updateUser(@RequestBody UserDetailsRequestModel user){
        //userService.updateUser(user);
        return null;
    }

    @DeleteMapping
    public void deleteUser(@PathVariable long id){
        userService.deleteUser(id);
    }

}