package com.transactions.payment.dto;


import com.transactions.payment.repository.PasswordMatches;
import com.transactions.payment.repository.ValidPassword;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@PasswordMatches.List({
        @PasswordMatches(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "passwords do not match!"
        )
})
public class UpdatePasswordDto {

  @NonNull
  @NotEmpty(message = "{password.notempty}")
  private String oldPassword;

  @ValidPassword
  @NonNull
  @NotEmpty(message = "{password.notempty}")
  private String password;

  @NonNull
  @NotEmpty(message = "{confirmPassword.notempty}")
  private String confirmPassword;
}
