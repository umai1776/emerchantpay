package com.transactions.payment.services;

import com.transactions.payment.dto.UpdatePasswordDto;
import com.transactions.payment.dto.UserDto;
import com.transactions.payment.exception.InvalidArgumentException;
import com.transactions.payment.exception.ResourceNotFoundException;
import com.transactions.payment.model.User;
import com.transactions.payment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Value("${com.transactions.payment.service.userservices.loginUrlMessage}")
    private String loginUrlMessage;
    @Value("${com.transactions.payment.service.userservices.changeUsersPasswordMessage}")
    private String changePasswordMessage;
    @Value("${com.transactions.payment.service.userservices.subjectChangePasswordMessage}")
    private String subjectChangePasswordMessage;


    public Optional<UserDetailsImpl> getCurrentUser() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return Optional.of(principal);
    }

    @Transactional
    public List<UserDto> showAllUsers() {
        List<User> users = userRepository.findAll();
        return  users.stream().map(this::mapFromUserToDto).collect(toList());
    }

    @Transactional
    public void createUser(UserDto userDto ) {
        User user = mapFromDtoToUser(userDto);
        userRepository.save(user);
    }

    private User mapFromDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAccountVerified(userDto.isAccountVerified());
        user.setRoles(userDto.getRoles());
        user.setPassword(userDto.getPassword());
        user.setConfirmPassword(userDto.getConfirmPassword());
        return user;
    }

    @Transactional
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("For id " + id));
        return mapFromUserToDto(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("For id " + id));
        userRepository.delete(user);
    }
    @Transactional
    public void updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("For id " + id));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAccountVerified(userDto.isAccountVerified());
        user.setRoles(userDto.getRoles());
        userRepository.save(user);
    }
    @Transactional
    public void updateUserPassword(Long id, UpdatePasswordDto passwordDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("For id " + id));

        if(!encoder.matches(passwordDto.getOldPassword(),user.getPassword()))
            throw new InvalidArgumentException("Incorrect old password");

        user.setPassword(encoder.encode(passwordDto.getPassword()));
        user.setConfirmPassword(encoder.encode(passwordDto.getConfirmPassword()));
        userRepository.save(user);
    }

    private UserDto mapFromUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setConfirmPassword(user.getConfirmPassword());
        userDto.setUsername(user.getUsername());
        userDto.setAccountVerified(user.isAccountVerified());
        userDto.setRoles(user.getRoles());
        return  userDto;
    }

}