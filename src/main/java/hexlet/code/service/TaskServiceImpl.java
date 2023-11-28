package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.model.Label;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UserServiceImpl userService;

    private final TaskStatusService taskStatusService;

    private final TaskRepository taskRepository;

    private final LabelService labelService;


    public List<Task> getAllTasks(Predicate predicate) {
        return (List<Task>) taskRepository.findAll(predicate);
    }

    @Override
    public Task create(TaskDto taskDto) {
        var task = fromDto(taskDto);
        return taskRepository.save(task);
    }

    @Override
    public Task update(TaskDto mayBeTaskDto, Task taskForUpdate) {
        final Task newTask = fromDto(mayBeTaskDto);

        taskForUpdate.setName(newTask.getName());
        taskForUpdate.setDescription(newTask.getDescription());
        taskForUpdate.setTaskStatus(newTask.getTaskStatus());
        taskForUpdate.setAuthor(newTask.getAuthor());
        taskForUpdate.setExecutor(newTask.getExecutor());

        return taskRepository.save(taskForUpdate);
    }



    private Task fromDto(TaskDto taskDto) {
        final User author = userService.getCurrentUser();

        final User executor = Optional.ofNullable(taskDto.getExecutorId())
            .map(userService::getUserById)
            .orElse(null);

        final TaskStatus taskStatus = Optional.ofNullable(taskDto.getTaskStatusId())
            .map(taskStatusService::getTaskStatusById)
            .orElse(null);

        final Set<Label> labels = Optional.ofNullable(taskDto.getLabelIds())
            .orElse(Set.of())
            .stream()
            .filter(Objects::nonNull)
            .map(labelService::getLabelById)
            .collect(Collectors.toSet());

        return Task.builder()
            .name(taskDto.getName())
            .description(taskDto.getDescription())
            .author(author)
            .executor(executor)
            .taskStatus(taskStatus)
            .labels(labels)
            .build();
    }


}
