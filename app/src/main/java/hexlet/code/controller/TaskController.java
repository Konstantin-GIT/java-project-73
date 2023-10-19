package hexlet.code.controller;

import hexlet.code.DTO.UserCreateDTO;
import hexlet.code.DTO.UserDTO;
import hexlet.code.DTO.UserUpdateDTO;
import hexlet.code.DTO.task.TaskCreateDTO;
import hexlet.code.DTO.task.TaskDTO;
import hexlet.code.DTO.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
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
@RequestMapping("${api.base-url}" + "/tasks")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskMapper taskMapper;

    @GetMapping(path = "/{id}")
    public Task show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + "not found"));
        //var userDTO = userMapper.map(user);

        return task;
    }

    /*@GetMapping
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
        return taskRepository.createUser(userData);
    }*/

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@PathVariable Long id, @RequestBody TaskUpdateDTO taskData) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        taskMapper.update(taskData, task);
        taskRepository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }


    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
