package hexlet.code.service;

import hexlet.code.DTO.UserCreateDTO;
import hexlet.code.DTO.UserDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    public UserDTO createUser(UserCreateDTO userData) {
        var user = userMapper.map(userData);
        userRepository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }
}
