package hexlet.code.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(unique = true)
    @Size(min = 1, message = "Название задачи должно быть не менее 1 символа")
    private String name;

    private String description;

    @NotNull
    @ManyToOne
    private TaskStatus taskStatus;

    @NotNull
    @ManyToOne
    private User author;

    @ManyToOne
    private User executor;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Label> labels;

    @CreatedDate
    private LocalDateTime createdAt;
}
