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
