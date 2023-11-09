package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;

import java.util.List;

public interface TaskService {
    Task create(TaskDto taskDto);

    Task update(TaskDto mayBeTaskDto, Task task);

    List<Task> getAllTasks(Predicate predicate);
}
