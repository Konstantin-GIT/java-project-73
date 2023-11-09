package hexlet.code.dto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDto {
    @NotBlank
    @Column(unique = true)
    @Size(min = 1, message = "Название статуса должно быть не менее 1 символа")
    private String name;

}
