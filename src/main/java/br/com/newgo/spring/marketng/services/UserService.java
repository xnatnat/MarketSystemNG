package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.UserDtos.CreateUserDto;
import br.com.newgo.spring.marketng.dtos.UserDtos.UserDto;
import br.com.newgo.spring.marketng.exceptions.ResourceAlreadyExistsException;
import br.com.newgo.spring.marketng.exceptions.ResourceNotFoundException;
import br.com.newgo.spring.marketng.models.User;
import br.com.newgo.spring.marketng.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
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
        throwIfEmailExists(userDto.getEmail());
        return mapToDto(save(mapToUser(userDto)));
    }

    public void throwIfEmailExists(String email) {
        if (existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("User with email " + email + " already exists.");
        }
    }

    protected boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDto findUserAndReturnDto(UUID id) {
        return mapToDto(findUserOrThrow(id));
    }

    public UserDto update(UUID id, CreateUserDto createUserDto) {
        var userData = findUserOrThrow(id);
        userData.setEmail(createUserDto.getEmail());
        userData.setPassword(createUserDto.getPassword());
        return mapToDto(save(userData));
    }

    @Transactional
    public void delete(UUID id){
        userRepository.delete(findUserOrThrow(id));
    }

    protected User findUserOrThrow(UUID id) {
        Optional<User> user = findById(id);
        return user.orElseThrow(
                () -> new ResourceNotFoundException("User not found."));
    }

    private UserDto mapToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    private User mapToUser(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    private User mapToUser(CreateUserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

}
