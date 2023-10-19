package hexlet.code.DTO.task;

import hexlet.code.DTO.UserDTO;
import hexlet.code.DTO.taskstatus.TaskStatusDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskUpdateDTO {
    private long id;

    private String name;

    private String description;

    private TaskStatusDTO taskStatus;

    private UserDTO author;

    private UserDTO executor;

    private Date createdAt;
}
