package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    @Autowired
    TaskStatusMapper taskStatusMapper;
    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus getTaskStatusById(long id) {
        return taskStatusRepository.findById(id).orElseThrow();

    }

    @Override
    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }


    @Override
    public TaskStatus createTaskStatus(final TaskStatusDto taskStatusDto) {
        TaskStatus newTaskStatus = new TaskStatus();
        newTaskStatus.setName(taskStatusDto.getName());

        return taskStatusRepository.save(newTaskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(final long id, final TaskStatusDto taskData) {
        TaskStatus taskStatusMayBe =  taskStatusRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));
        taskStatusMayBe.setName(taskData.getName());
        return taskStatusRepository.save(taskStatusMayBe);
    }

    @Override
    public void deleteTaskStatus(final long id) {
        var taskStatus = taskStatusRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + "not found"));
        taskStatusRepository.delete(taskStatus);
    }
}
