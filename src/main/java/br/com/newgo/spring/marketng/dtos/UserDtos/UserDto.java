package br.com.newgo.spring.marketng.dtos.UserDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDto {

    @NotBlank
    @Email
    private String email;
    @NotNull
    private UUID id;

}
