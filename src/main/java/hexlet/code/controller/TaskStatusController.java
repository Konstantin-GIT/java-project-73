package hexlet.code.controller;

import hexlet.code.dto.taskstatus.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskStatusServiceImpl;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    TaskStatusServiceImpl taskStatusService;

    @GetMapping(ID)
    public TaskStatus show(@PathVariable Long id) {
        return taskStatusService.getTaskStatus(id);
    }

    @GetMapping
    public List<TaskStatus> index() {
        return taskStatusService.getTaskStatuses();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createNew(@RequestBody  @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }


    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public TaskStatus update(@PathVariable Long id, @RequestBody @Valid TaskStatusDto taskStatusData) {
        return taskStatusService.updateTaskStatus(id, taskStatusData);
    }

    @DeleteMapping(ID)
    public void delete(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
    }

}
