package br.com.newgo.spring.marketng.controllers;


import br.com.newgo.spring.marketng.dtos.UserDtos.CreateUserDto;
import br.com.newgo.spring.marketng.dtos.UserDtos.UserDto;
import br.com.newgo.spring.marketng.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody @Valid CreateUserDto userDto,
                                        UriComponentsBuilder uriComponentsBuilder){
        var userData = userService.saveAndReturnDto(userDto);
        var uri = uriComponentsBuilder.path("/api/v1/users/{id}").buildAndExpand(userData.getId()).toUri();
        return ResponseEntity.created(uri).body(userData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable(value = "id") UUID id){
        return ResponseEntity.ok(userService.findUserAndReturnDto(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<UserDto> update(@PathVariable(value = "id") UUID id,
                                             @RequestBody @Valid CreateUserDto createUserDto){
        return ResponseEntity.ok(userService.update(id, createUserDto));
    }
}
