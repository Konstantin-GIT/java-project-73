package hexlet.code.controller;

import hexlet.code.dto.TaskDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task found",
            content = @Content(schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = Task.class)))
    })
    @GetMapping(ID)
    @ResponseStatus(OK)
    public Task show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return task;
    }


    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks found",
            content = @Content(schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "404", description = "Tasks not found",
            content = @Content(schema = @Schema(implementation = Task.class)))
    })
    @GetMapping
    @ResponseStatus(OK)
    public List<Task> index(Predicate predicate) {
        return taskService.getAllTasks(predicate);
    }


    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created",
            content = @Content(schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "422", description = "Cannot create task with this data",
            content = @Content(schema = @Schema(implementation = Task.class)))
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public Task create(@RequestBody @Valid TaskDto taskDto) {
        return taskService.create(taskDto);
    }


    @Operation(summary = "Update task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated",
            content = @Content(schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "422", description = "Cannot update task with this data",
            content = @Content(schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = Task.class)))
    })
    @PutMapping(ID)
    @ResponseStatus(OK)
    public Task update(@PathVariable Long id, @RequestBody  @Valid TaskDto taskDto) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        var taskForUpdate = taskService.update(taskDto, task);
        var updatedTask = taskRepository.save(taskForUpdate);
        return updatedTask;
    }


    @Operation(summary = "Delete task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task deleted",
            content = @Content(schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = Task.class)))
    })
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
