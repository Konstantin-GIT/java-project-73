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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.MAPPER;
import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.*;
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

        final Task expectedTask = taskRepository.findAll().get(0);
        final MockHttpServletResponse response = utils.perform(
                get(TASK_CONTROLLER_PATH + "/{id}", expectedTask.getId()),
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

        final Task expectedTask = taskRepository.findAll().get(0);

        utils.perform(
                get(TASK_CONTROLLER_PATH + "/{id}", expectedTask.getId() + 1),
                TEST_USERNAME
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTask() throws Exception {
        utils.createTask(buildTaskDto());

        final Long taskId = taskRepository.findByName(TEST_TASK_NAME).orElseThrow().getId();
        final Task currentTask = taskRepository.findAll().get(0);
        final TaskDto taskDtoForUpdate = buildTaskDtoForUpdate(currentTask);

        final MockHttpServletRequestBuilder updateRequest = put(
            TASK_CONTROLLER_PATH + "/{id}", taskId
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

        final Long taskId = taskRepository.findByName(TEST_TASK_NAME).orElseThrow().getId();
        final Task currentTask = taskRepository.findAll().get(0);
        final TaskDto taskDtoForUpdate = buildTaskDtoForUpdate(currentTask);
        taskDtoForUpdate.setName("");

        final MockHttpServletRequestBuilder updateRequest = put(
            TASK_CONTROLLER_PATH + "/{id}", taskId
        )
            .content(MAPPER.writeValueAsString(taskDtoForUpdate))
            .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deleteTaskByOwner() throws Exception {
        utils.createTask(buildTaskDto());

        final Long taskId = taskRepository.findByName(TEST_TASK_NAME).orElseThrow().getId();

        utils.perform(delete(TASK_CONTROLLER_PATH + "/{id}", taskId), TEST_USERNAME)
            .andExpect(status().isOk());

        assertThat(taskRepository.existsById(taskId)).isFalse();
    }

    @Test
    public void deleteTaskByNotOwner() throws Exception {
        utils.createTask(buildTaskDto());

        final Long taskId = taskRepository.findByName(TEST_TASK_NAME).orElseThrow().getId();

        utils.perform(delete(TASK_CONTROLLER_PATH + "/{id}", taskId), TEST_USERNAME_2)
            .andExpect(status().isForbidden());
    }

    private TaskDto buildTaskDto() {
        final User user = userRepository.findByEmail(TEST_USERNAME).orElseThrow();
        final TaskStatus taskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).orElseThrow();

        //final Label label = labelRepository.findByName(TEST_LABEL_NAME).orElseThrow();

        final TaskDto taskDto = new TaskDto();
        taskDto.setName(TEST_TASK_NAME);
        taskDto.setDescription(TEST_TASK_DESCRIPTION);
        taskDto.setTaskStatusId(taskStatus.getId());
        //taskDto.setAuthorId(user.getId());
        taskDto.setExecutorId(user.getId());
        //taskDto.setLabelIds(Set.of(label.getId()));

        return taskDto;
    }

    private static TaskDto buildTaskDtoForUpdate(Task task) {
        final String updatedTaskName = "updatedTaskName";
        final String updatedTaskDescription = "updatedTaskDescription";
        //final Label label = task.getLabels().stream().findFirst().orElseThrow();

        final TaskDto taskDtoForUpdate = new TaskDto();
        taskDtoForUpdate.setName(updatedTaskName);
        taskDtoForUpdate.setDescription(updatedTaskDescription);
        taskDtoForUpdate.setTaskStatusId(task.getTaskStatus().getId());
        //taskDtoForUpdate.setAuthorId(task.getAuthor().getId());
        taskDtoForUpdate.setExecutorId(task.getExecutor().getId());
        //taskDtoForUpdate.setLabelIds(Set.of(label.getId()));

        return taskDtoForUpdate;
    }
}
