package com.transactions.payment.controller;

import com.transactions.payment.dto.UpdatePasswordDto;
import com.transactions.payment.dto.UserDto;
import com.transactions.payment.model.User;
import com.transactions.payment.repository.UserRepository;
import com.transactions.payment.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UserController {

    @Autowired
    UserServices userServices;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userServices.showAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(userServices.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long id,
                                           @Valid @RequestBody UserDto userDto) {
        userServices.updateUser(id, userDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/password/{id}")
    public ResponseEntity<User> updateUserPass(@PathVariable(value = "id") Long id,
                                               @Valid @RequestBody UpdatePasswordDto userDto) {
        userServices.updateUserPassword(id, userDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        userServices.deleteUser(id);
        return ResponseEntity.ok().build();
    }
/*
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/updateUserPasswordByAdmin/{id}")
    public ResponseEntity<User> updateUserPasswordByAdmin(@PathVariable(value = "id") Long id) {
        userServices.updateUserPasswordByAdmin(id);
        return ResponseEntity.ok().build();
    }*/
}