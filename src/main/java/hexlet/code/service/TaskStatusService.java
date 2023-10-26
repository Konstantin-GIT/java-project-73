package hexlet.code.service;
//
//import hexlet.code.DTO.backup.taskstatus.TaskStatusCreateDTO;
//import hexlet.code.DTO.backup.taskstatus.TaskStatusDTO;
//import hexlet.code.mapper.TaskStatusMapper;
//import hexlet.code.repository.TaskStatusRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TaskStatusService {
//
//    @Autowired
//    private TaskStatusMapper taskStatusMapper;
//    @Autowired
//    TaskStatusRepository taskStatusRepository;
//
//    public TaskStatusDTO createTaskStatus(TaskStatusCreateDTO taskStatusData) {
//        var taskStatus = taskStatusMapper.map(taskStatusData);
//        taskStatusRepository.save(taskStatus);
//        var userDTO = taskStatusMapper.map(taskStatus);
//        return userDTO;
//    }
//}
