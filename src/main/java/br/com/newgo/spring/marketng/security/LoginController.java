package br.com.newgo.spring.marketng.security;

import br.com.newgo.spring.marketng.dtos.UserDtos.CreateUserDto;
import br.com.newgo.spring.marketng.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    final AuthenticationManager authenticationManager;
    final AuthenticationService authenticationService;
    final LoginService loginService;

    public LoginController(AuthenticationManager authenticationManager, AuthenticationService authenticationService, LoginService loginService) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<JwtTokenDto> login(@RequestBody @Valid CreateUserDto userDto){
        var token = new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());
        try {
            var authentication = authenticationManager.authenticate(token);
            return ResponseEntity.ok(new JwtTokenDto(authenticationService.generateToken((User) authentication.getPrincipal())));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtTokenDto("Falha na autenticação: " + e.getMessage()));
        }
    }

}


