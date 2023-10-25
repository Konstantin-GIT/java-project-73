package hexlet.code.DTO.backup.task;

import hexlet.code.DTO.backup.UserDTO;
import hexlet.code.DTO.backup.taskstatus.TaskStatusDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class TaskDTO {
    private long id;

    private String name;

    private String description;

    private TaskStatusDTO taskStatusDTO;

    private UserDTO author;

    private UserDTO executor;

    private Date createdAt;
}

