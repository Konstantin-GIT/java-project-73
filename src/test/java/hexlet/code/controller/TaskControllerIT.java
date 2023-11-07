package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskControllerIT {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    //@Autowired
    //private LabelRepository labelRepository;
    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void before() throws Exception {
        utils.regUser(TEST_USER_DTO);
        utils.createTaskStatus(TASK_STATUS_DTO);
       // utils.createLabel(LABEL_DTO);
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void createTask() throws Exception {
        //assertThat(0).isEqualTo(taskRepository.count());

        final MockHttpServletResponse response = utils.createTask(buildTaskDto())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();

        final Task savedTask = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(1).isEqualTo(taskRepository.count());
        assertThat(taskRepository.getReferenceById(savedTask.getId())).isNotNull();
    }

    private TaskDto buildTaskDto() {
        final User user = userRepository.findByEmail(TEST_USERNAME).orElseThrow();
        final TaskStatus taskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).orElseThrow();

        //final Label label = labelRepository.findByName(TEST_LABEL_NAME).orElseThrow();

        final TaskDto taskDto = new TaskDto();
        taskDto.setName(TEST_TASK_NAME);
        taskDto.setDescription(TEST_TASK_DESCRIPTION);
        taskDto.setTaskStatusId(taskStatus.getId());
        taskDto.setAuthorId(user.getId());
        taskDto.setExecutorId(user.getId());
        //taskDto.setLabelIds(Set.of(label.getId()));

        return taskDto;
    }
}
