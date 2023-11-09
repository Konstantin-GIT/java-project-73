package hexlet.code.controller;

import hexlet.code.dto.TaskDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import java.util.List;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
@AllArgsConstructor
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";
    private static final String ONLY_AUTHOR_BY_ID = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
            """;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskService taskService;

    @GetMapping(ID)
    @ResponseStatus(OK)
    public Task show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return task;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Task> index(Predicate predicate) {
        return taskService.getAllTasks(predicate);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Task create(@RequestBody @Valid TaskDto taskDto) {
        return taskService.create(taskDto);
    }

    @PutMapping(ID)
    @ResponseStatus(OK)
    public Task update(@PathVariable Long id, @RequestBody  @Valid TaskDto mayBeTaskDto) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        var taskForUpdate = taskService.update(mayBeTaskDto, task);
        var updatedTask = taskRepository.save(taskForUpdate);
        return updatedTask;
    }

    @DeleteMapping(ID)
    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
