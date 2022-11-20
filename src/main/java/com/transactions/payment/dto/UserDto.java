package com.transactions.payment.dto;

import com.transactions.payment.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private boolean isAccountVerified;
    private Set<Role> roles = new HashSet<>();

    public UserDto() {
    }
    public UserDto(Long id, String username, String email, String password, String confirmPassword, boolean isAccountVerified, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.isAccountVerified = isAccountVerified;
        this.roles = roles;
    }
}
