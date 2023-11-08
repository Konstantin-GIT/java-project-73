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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
@AllArgsConstructor
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskServiceImpl taskService;

    @GetMapping(path = "/{id}")
    @ResponseStatus(OK)
    public Task show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        return task;
    }


    @PutMapping(path = "/{id}")
    @ResponseStatus(OK)
    public Task update(@PathVariable Long id, @RequestBody  @Valid TaskDto mayBeTaskDto) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        var taskForUpdate = taskService.update(mayBeTaskDto, task);
        var updatedTask = taskRepository.save(taskForUpdate);
        return updatedTask;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Task create(@RequestBody TaskDto taskDto) {

        return taskService.create(taskDto);

    }


    @DeleteMapping(path = "/{id}")
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }

}
