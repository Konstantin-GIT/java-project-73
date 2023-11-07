package hexlet.code.dto;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotBlank
    @Size(min = 1, message = "Название задачи должно быть не менее 1 символа")
    private String name;

    private String description;

    //@NotBlank
    private Long taskStatusId;

    //@NotBlank
    private Long authorId;

    private Long executorId;

}
