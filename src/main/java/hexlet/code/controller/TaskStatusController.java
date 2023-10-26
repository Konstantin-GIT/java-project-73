package hexlet.code.controller;

//import hexlet.code.DTO.taskstatus.TaskStatusCreateDTO;
//import hexlet.code.DTO.taskstatus.TaskStatusDTO;
//import hexlet.code.DTO.taskstatus.TaskStatusUpdateDTO;
//import hexlet.code.exception.ResourceNotFoundException;
//import hexlet.code.mapper.TaskStatusMapper;
//import hexlet.code.model.TaskStatus;
//import hexlet.code.repository.TaskStatusRepository;
//import hexlet.code.service.TaskStatusService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("${base-url}" + "/statuses")
//public class TaskStatusController {
//
//
//    @Autowired
//    TaskStatusRepository taskStatusRepository;
//
//    @Autowired
//    private TaskStatusMapper taskStatusMapper;
//
//    @Autowired
//    TaskStatusService taskStatusService;
//
//    @GetMapping(path = "/{id}")
//    public TaskStatus show(@PathVariable Long id) {
//        var taskStatus = taskStatusRepository.findById(id)
//            .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + "not found"));
//        //var userDTO = userMapper.map(user);
//
//        return taskStatus;
//    }
//
//    @GetMapping
//    public List<TaskStatusDTO> index() {
//        var taskStatuses = taskStatusRepository.findAll();
//
//        var taskStatusesDTO = new ArrayList<TaskStatusDTO>();
//        for (TaskStatus taskStatus : taskStatuses) {
//            taskStatusesDTO.add(taskStatusMapper.map(taskStatus));
//        }
//        return taskStatusesDTO;
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public TaskStatusDTO create(@RequestBody TaskStatusCreateDTO taskStatusData) {
//        return taskStatusService.createTaskStatus(taskStatusData);
//    }
//
//
//    @PutMapping(path = "/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public TaskStatusDTO update(@PathVariable Long id, @RequestBody TaskStatusUpdateDTO taskStatusData) {
//        var taskStatus = taskStatusRepository.findById(id)
//            .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + "not found"));
//        taskStatusMapper.update(taskStatusData, taskStatus);
//        taskStatusRepository.save(taskStatus);
//        var taskStatusDTO = taskStatusMapper.map(taskStatus);
//        return taskStatusDTO;
//    }
//
//    @DeleteMapping(path = "/{id}")
//    public void delete(@PathVariable Long id) {
//        taskStatusRepository.deleteById(id);
//    }
//
//}
