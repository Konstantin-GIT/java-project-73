package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    TaskStatusServiceImpl taskStatusService;

    @Autowired
    TaskRepository taskRepository;

    @Override
    public Task create(TaskDto taskDto) {
        var task = fromDto(taskDto);

        return taskRepository.save(task);
    }

    @Override
    public Task update(TaskDto mayBeTaskDto, Task task) {
        var name = mayBeTaskDto.getName();
        var description = mayBeTaskDto.getDescription();
        var taskStatusId = mayBeTaskDto.getTaskStatusId();
        var executorId = mayBeTaskDto.getExecutorId();
        if (taskStatusId != null) {
            var taskStatus = taskStatusService.getTaskStatus(taskStatusId);
            task.setTaskStatus(taskStatus);
        }
        if (name != null) {
            task.setName(name);
        }
        if (executorId != null) {
            var executor = userService.getUser(executorId);
            task.setExecutor(executor);
        }
        if (description != null) {
            task.setDescription(description);
        }
        return task;
    }

    private Task fromDto(TaskDto taskDto) {
        Task task = new Task();
        var author = userService.getCurrentUser();
        var taskStatus = taskStatusService.getTaskStatus(taskDto.getTaskStatusId());
        var executor = userService.getUser(taskDto.getExecutorId());
        task.setName(taskDto.getName());
        task.setDescription((taskDto.getDescription()));
        task.setAuthor(author);
        if (taskDto.getExecutorId() != null) {
            task.setExecutor(userService.getUser(taskDto.getExecutorId()));
        }
        task.setTaskStatus(taskStatus);
        return task;
    }
}
