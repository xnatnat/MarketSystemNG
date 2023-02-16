package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.UserDtos.CreateUserDto;
import br.com.newgo.spring.marketng.dtos.UserDtos.UserDto;
import br.com.newgo.spring.marketng.models.User;
import br.com.newgo.spring.marketng.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
public class UserService {
    final UserRepository userRepository;
    final ModelMapper modelMapper;
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
    @Transactional
    public User save(User user){
        return userRepository.save(user);
    }

    public UserDto saveAndReturnDto(CreateUserDto userDto){
        return mapToDto(save(mapToUser(userDto)));
    }
    public UserDto findUserAndReturnDto(UUID id) {
        return mapToDto(findById(id).orElseThrow());
    }

    public UserDto update(UUID id, CreateUserDto createUserDto) {
        var userData = findById(id).orElseThrow();
        userData.setEmail(createUserDto.getEmail());
        userData.setPassword(createUserDto.getPassword());
        return mapToDto(save(userData));
    }

    @Transactional
    public void delete(UUID id){
        userRepository.delete(findById(id).orElseThrow());
    }

    private UserDto mapToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    private User mapToUser(CreateUserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

}
