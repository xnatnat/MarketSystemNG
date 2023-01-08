package br.com.newgo.spring.marketng.dtos.UserDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
