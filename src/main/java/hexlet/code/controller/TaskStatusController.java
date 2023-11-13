package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

import java.util.List;

@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    @Autowired
    private TaskStatusServiceImpl taskStatusService;

    @Operation(summary = "Get task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status found",
            content = @Content(schema = @Schema(implementation = TaskStatus.class))),
        @ApiResponse(responseCode = "404", description = "Task status not found",
            content = @Content(schema = @Schema(implementation = TaskStatus.class)))
    })
    @GetMapping(ID)
    public TaskStatus show(@PathVariable Long id) {
        return taskStatusService.getTaskStatusById(id);
    }

    @Operation(summary = "Get all task statuses")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task statuses found",
            content = @Content(schema = @Schema(implementation = TaskStatus.class))),
        @ApiResponse(responseCode = "404", description = "Task statuses not found",
            content = @Content(schema = @Schema(implementation = TaskStatus.class)))
    })
    @GetMapping
    public List<TaskStatus> index() {
        return taskStatusService.getTaskStatuses();
    }

    @Operation(summary = "Create a new task status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task status created",
            content = @Content(schema = @Schema(implementation = TaskStatus.class))),
        @ApiResponse(responseCode = "422", description = "Cannot create task status with this data",
            content = @Content(schema = @Schema(implementation = TaskStatus.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createNew(@RequestBody  @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @Operation(summary = "Update task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status updated",
            content = @Content(schema = @Schema(implementation = TaskStatus.class))),
        @ApiResponse(responseCode = "422", description = "Cannot update task status with this data",
            content = @Content(schema = @Schema(implementation = TaskStatus.class))),
        @ApiResponse(responseCode = "404", description = "Task status not found",
            content = @Content(schema = @Schema(implementation = TaskStatus.class)))
    })
    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public TaskStatus update(@PathVariable Long id, @RequestBody @Valid TaskStatusDto taskStatusData) {
        return taskStatusService.updateTaskStatus(id, taskStatusData);
    }

    @Operation(summary = "Delete task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status deleted",
            content = @Content(schema = @Schema(implementation = TaskStatus.class))),
        @ApiResponse(responseCode = "404", description = "Task status not found",
            content = @Content(schema = @Schema(implementation = TaskStatus.class)))
    })
    @DeleteMapping(ID)
    public void delete(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
    }

}
