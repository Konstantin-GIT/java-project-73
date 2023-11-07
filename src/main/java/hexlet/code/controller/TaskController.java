package hexlet.code.controller;

import hexlet.code.dto.TaskDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
@AllArgsConstructor
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskServiceImpl taskService;
/*
    @GetMapping(path = "/{id}")
    public Task show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + "not found"));
        //var userDTO = userMapper.map(user);

        return task;
    }


    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto update(@PathVariable Long id, @RequestBody TaskDto taskData) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        taskMapper.update(taskData, task);
        taskRepository.save(task);
        var taskDto = taskMapper.map(task);
        return taskDto;
    }*/

    @PostMapping
    @ResponseStatus(CREATED)
    public Task create(@RequestBody TaskDto taskDto) {

        return taskService.create(taskDto);

    }
/*

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
    */
}
