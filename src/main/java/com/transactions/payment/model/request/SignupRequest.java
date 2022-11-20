package com.transactions.payment.model.request;

import com.transactions.payment.repository.PasswordMatches;
import com.transactions.payment.repository.ValidPassword;
import lombok.*;
import javax.validation.constraints.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@PasswordMatches.List({
        @PasswordMatches(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Password do not match!"
        )
})
public class SignupRequest {
    @NotBlank(message = "{com.transactions.payment.signuprequest.username.notblank}")
    @Size(message = "{com.transactions.payment.signuprequest.username.length}", min = 3, max = 15)
    private String username;

    @Email(message = "{com.transactions.payment.signuprequest.email.valid}")
    @NotEmpty(message = "{com.transactions.payment.signuprequest.email.notempty}")
    @Size(max = 50, message = "{com.transactions.payment.signuprequest.email.size}")
    @Pattern(regexp = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$", message = "{com.transactions.payment.signuprequest.email.regexp}")
    private String email;

    private Set<String> role;

    @ValidPassword
    @NonNull
    @NotEmpty(message = "{com.transactions.payment.signuprequest.password.notempty}")
    private String password;

    @ValidPassword
    @NonNull
    @NotEmpty(message = "{com.transactions.payment.signuprequest.confirmPassword.notempty}")
    private String confirmPassword;
}
