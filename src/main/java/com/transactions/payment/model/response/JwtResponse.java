package com.transactions.payment.model.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private boolean accountVerified;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email, boolean accountVerified, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.accountVerified = accountVerified;
        this.roles = roles;
    }
}
