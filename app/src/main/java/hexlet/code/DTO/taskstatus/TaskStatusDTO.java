package hexlet.code.DTO.taskstatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class TaskStatusDTO {

    private long id;
    private String name;
    private Date createdAt;

}
