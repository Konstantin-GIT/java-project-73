package hexlet.code.dto.taskstatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDto {
    @NotBlank
    @Column(unique = true)
    @Size(min = 1, message = "Название статуса должно быть не менее 1 символа")
    private String name;

}
