package hexlet.code.util;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import lombok.Setter;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;

@Getter
@Setter
@Component
public class ModelGenerator {
    public Model<Task> taskModel;
    private Model<User> authorModel;
    private Model<User> executorModel;
    private Model<TaskStatus> taskStatusModel;


    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        taskModel = Instancio.of(Task.class)
            .ignore(Select.field((Task::getId)))
            .supply(Select.field((Task::getName)), () -> faker.lorem().word())
            .supply(Select.field((Task::getDescription)), () -> faker.lorem().words(5))
            .toModel();

        authorModel = Instancio.of(User.class)
            .ignore(Select.field((User::getId)))
            .supply(Select.field((User::getFirstname)), () -> faker.lorem().word())
            .supply(Select.field((User::getLastname)), () -> faker.lorem().word())
            .supply(Select.field((User::getEmail)), () -> faker.internet().emailAddress())
            .toModel();

        executorModel = Instancio.of(User.class)
            .ignore(Select.field((User::getId)))
            .supply(Select.field((User::getFirstname)), () -> faker.lorem().word())
            .supply(Select.field((User::getLastname)), () -> faker.lorem().word())
            .supply(Select.field((User::getEmail)), () -> faker.internet().emailAddress())
            .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
            .ignore(Select.field((TaskStatus::getId)))
            .supply(Select.field((TaskStatus::getName)), () -> faker.lorem().word())
            .ignore(Select.field((TaskStatus::getCreatedAt)))
            .toModel();
    }
}
