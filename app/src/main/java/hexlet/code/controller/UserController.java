package hexlet.code.controller;

import hexlet.code.DTO.UserCreateDTO;
import hexlet.code.DTO.UserDTO;
import hexlet.code.DTO.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${api.base-url}" + "/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}")
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        var userDTO = userMapper.map(user);

        return userDTO;
    }

    @GetMapping
    public List<UserDTO> index() {
        var users = userRepository.findAll();
        var usersDTO = new ArrayList<UserDTO>();
        for (User user : users) {
            usersDTO.add(userMapper.map(user));
        }
        return usersDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody UserCreateDTO userData) {
        return userService.createUser(userData);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@PathVariable Long id, @RequestBody UserUpdateDTO userData) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        userMapper.update(userData, user);
        userRepository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

}
