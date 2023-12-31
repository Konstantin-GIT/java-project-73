package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.util.List;
import java.util.Set;
import static hexlet.code.controller.TaskController.ID;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.MAPPER;
import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.TEST_USER_DTO;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.utils.TestUtils.TEST_LABEL_DTO;
import static hexlet.code.utils.TestUtils.TASK_STATUS_DTO;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.TEST_USERNAME_2;
import static hexlet.code.utils.TestUtils.TEST_LABEL_NAME;
import static hexlet.code.utils.TestUtils.TEST_TASK_DESCRIPTION;
import static hexlet.code.utils.TestUtils.TEST_TASK_NAME;
import static hexlet.code.utils.TestUtils.TEST_TASK_STATUS_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
@PropertySource(value = "classpath:/config/application-test.yml")
public class TaskControllerIT {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void before() throws Exception {
        utils.regUser(TEST_USER_DTO);
        utils.createTaskStatus(TASK_STATUS_DTO);
        utils.createLabel(TEST_LABEL_DTO);
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }


    @Test
    public void getAllTasks() throws Exception {
        utils.createDefaultTask();
        final var response = utils.perform(get(TASK_CONTROLLER_PATH), TEST_USERNAME)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<Task> expected = taskRepository.findAll();
        Assertions.assertThat(tasks)
            .containsAll(expected);
    }

    @Test
    public void getSortedAllTasks() throws Exception {
        utils.createDefaultTask();

        final Task expectedTask = taskRepository.findByName(TEST_TASK_NAME).orElseThrow();
        final Long expectedLabelId = expectedTask.getLabels().stream().findFirst().orElseThrow().getId();

        final String queryString = String.format("?taskStatusId=%d&executorId=%d&authorId=%d&labelsId=%d",
            expectedTask.getTaskStatus().getId(),
            expectedTask.getExecutor().getId(),
            expectedTask.getAuthor().getId(),
            expectedLabelId);

        final MockHttpServletResponse response = utils.perform(
                get(TASK_CONTROLLER_PATH + queryString),
                TEST_USERNAME
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() { });

        final List<Task> expected = taskRepository.findAll();

        Assertions.assertThat(tasks).containsAll(expected);
    }

    @Test
    public void getSortedAllTasksFail() throws Exception {
        utils.createDefaultTask();

        final Task expectedTask = taskRepository.findByName(TEST_TASK_NAME).orElseThrow();
        final Long expectedLabelId = expectedTask.getLabels().stream().findFirst().orElseThrow().getId();

        final Long wrongAuthorId = expectedTask.getAuthor().getId() + 1;

        final String queryString = String.format("?taskStatusId=%d&executorId=%d&authorId=%d&labelsId=%d",
            expectedTask.getTaskStatus().getId(),
            expectedTask.getExecutor().getId(),
            wrongAuthorId,
            expectedLabelId);

        final MockHttpServletResponse response = utils.perform(
                get(TASK_CONTROLLER_PATH + queryString),
                TEST_USERNAME
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final List<Task> expected = taskRepository.findAll();

        Assertions.assertThat(tasks).isNotIn(expected);
    }



    @Test
    public void createTask() throws Exception {

        final MockHttpServletResponse response = utils.createTask(buildTaskDto())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();

        final Task savedTask = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(1).isEqualTo(taskRepository.count());
        assertThat(taskRepository.getReferenceById(savedTask.getId())).isNotNull();
    }

    @Test
    public void createTaskFail() throws Exception {
        final TaskDto taskDto = new TaskDto();

        final MockHttpServletRequestBuilder request = post(TASK_CONTROLLER_PATH)
            .content(MAPPER.writeValueAsString(taskDto))
            .contentType(APPLICATION_JSON);

        utils.perform(request).andExpect(status().isForbidden());
    }

    @Test
    public void twiceCreateIdenticalTaskFail() throws Exception {
        utils.createTask(buildTaskDto()).andExpect(status().isCreated());
        utils.createTask(buildTaskDto()).andExpect(status().isUnprocessableEntity());

        assertThat(1).isEqualTo(taskRepository.count());
    }

    @Test
    public void getTaskById() throws Exception {
        utils.createTask(buildTaskDto());

        final Task expectedTask = taskRepository.findByName(TEST_TASK_NAME).orElseThrow();

        final MockHttpServletResponse response = utils.perform(
                get(TASK_CONTROLLER_PATH + ID, expectedTask.getId()),
                TEST_USERNAME
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        final Task task = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(expectedTask.getId()).isEqualTo(task.getId());
        assertThat(expectedTask.getName()).isEqualTo(task.getName());
        assertThat(expectedTask.getDescription()).isEqualTo(task.getDescription());
        assertThat(expectedTask.getTaskStatus().getName()).isEqualTo(task.getTaskStatus().getName());
        assertThat(expectedTask.getAuthor().getEmail()).isEqualTo(task.getAuthor().getEmail());
        assertThat(expectedTask.getExecutor().getEmail()).isEqualTo(task.getExecutor().getEmail());
    }

    @Test
    public void getTaskByIdFail() throws Exception {
        utils.createTask(buildTaskDto());

        final Task expectedTask = taskRepository.findByName(TEST_TASK_NAME).get();

        utils.perform(
                delete(TASK_CONTROLLER_PATH + ID, expectedTask.getId()),
                TEST_USERNAME
            )
            .andExpect(status().isOk());

        Long notExistTaskId = expectedTask.getId();

        utils.perform(
                get(TASK_CONTROLLER_PATH + ID, notExistTaskId),
                TEST_USERNAME
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTask() throws Exception {
        utils.createTask(buildTaskDto());

        final var task = taskRepository.findByName(TEST_TASK_NAME).orElseThrow();
        final Long taskId = task.getId();
        final TaskDto taskDtoForUpdate = buildTaskDtoForUpdate(task);

        final MockHttpServletRequestBuilder updateRequest = put(
            TASK_CONTROLLER_PATH + ID, taskId
        )
            .content(MAPPER.writeValueAsString(taskDtoForUpdate))
            .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        final Task expectedUpdatedTask = taskRepository.findById(taskId).orElseThrow();

        assertThat(expectedUpdatedTask.getId()).isEqualTo(taskId);
        assertThat(expectedUpdatedTask.getDescription()).isEqualTo(taskDtoForUpdate.getDescription());
        assertThat(expectedUpdatedTask.getName()).isEqualTo(taskDtoForUpdate.getName());
    }

    @Test
    public void updateTaskFail() throws Exception {
        utils.createTask(buildTaskDto());

        final var task = taskRepository.findByName(TEST_TASK_NAME).orElseThrow();
        final Long taskId = task.getId();
        final TaskDto taskDtoForUpdate = buildTaskDtoForUpdate(task);
        // set wrong(blank) name
        taskDtoForUpdate.setName("");

        final MockHttpServletRequestBuilder updateRequest = put(
            TASK_CONTROLLER_PATH + ID, taskId
        )
            .content(MAPPER.writeValueAsString(taskDtoForUpdate))
            .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deleteTaskByOwner() throws Exception {
        utils.createTask(buildTaskDto());

        final Long taskId = taskRepository.findByName(TEST_TASK_NAME).orElseThrow().getId();

        utils.perform(delete(TASK_CONTROLLER_PATH + ID, taskId), TEST_USERNAME)
            .andExpect(status().isOk());

        assertThat(taskRepository.existsById(taskId)).isFalse();
    }

    @Test
    public void deleteTaskByNotOwner() throws Exception {
        utils.createTask(buildTaskDto());

        final Long taskId = taskRepository.findByName(TEST_TASK_NAME).orElseThrow().getId();

        utils.perform(delete(TASK_CONTROLLER_PATH + ID, taskId), TEST_USERNAME_2)
            .andExpect(status().isForbidden());
    }

    private TaskDto buildTaskDto() {
        final User user = userRepository.findByEmail(TEST_USERNAME).orElseThrow();
        final TaskStatus taskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).orElseThrow();

        final Label label = labelRepository.findByName(TEST_LABEL_NAME).orElseThrow();

        final TaskDto taskDto = new TaskDto();
        taskDto.setName(TEST_TASK_NAME);
        taskDto.setDescription(TEST_TASK_DESCRIPTION);
        taskDto.setTaskStatusId(taskStatus.getId());
        taskDto.setAuthorId(user.getId());
        taskDto.setExecutorId(user.getId());
        taskDto.setLabelIds(Set.of(label.getId()));

        return taskDto;
    }

    private static TaskDto buildTaskDtoForUpdate(Task task) {
        final String updatedTaskName = "updatedTaskName";
        final String updatedTaskDescription = "updatedTaskDescription";
        final Label label = task.getLabels().stream().findFirst().orElseThrow();

        final TaskDto taskDtoForUpdate = new TaskDto();
        taskDtoForUpdate.setName(updatedTaskName);
        taskDtoForUpdate.setDescription(updatedTaskDescription);
        taskDtoForUpdate.setTaskStatusId(task.getTaskStatus().getId());
        taskDtoForUpdate.setAuthorId(task.getAuthor().getId());
        taskDtoForUpdate.setExecutorId(task.getExecutor().getId());
        taskDtoForUpdate.setLabelIds(Set.of(label.getId()));

        return taskDtoForUpdate;
    }
}
