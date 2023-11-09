package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.UserDto;
import hexlet.code.dto.taskstatus.TaskStatusDto;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@Component
public class TestUtils {

    public static final String TEST_USERNAME = "email_test@gmail.com";
    public static final String TEST_USERNAME_2 = "email2_test@gmail.com";
    public static final String TEST_TASK_NAME = "TaskName";
    public static final String TEST_TASK_DESCRIPTION = "TaskDescription";
    public static final String TEST_TASK_STATUS_NAME = "task status name";
    public static final String TEST_LABEL_NAME = "labelName1_test";
    public static final String TEST_LABEL_NAME_2 = "labelName2_test";
    public static final UserDto TEST_USER_DTO = new UserDto(
        TEST_USERNAME,
        "test_first_name",
        "test_last_name",
        "test_pwd"
    );

    public static final TaskStatusDto TASK_STATUS_DTO = new TaskStatusDto(TEST_TASK_STATUS_NAME);
    public static final LabelDto TEST_LABEL_DTO = new LabelDto(TEST_LABEL_NAME);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;


    public void tearDown() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).get();
    }

    public ResultActions regDefaultUser() throws Exception {
        return regUser(TEST_USER_DTO);
    }

    public ResultActions regUser(final UserDto dto) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
            .content(asJson(dto))
            .contentType(APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private final TaskStatusDto testDtoForTaskStatus = new TaskStatusDto(TEST_TASK_STATUS_NAME);
    public ResultActions createDefaultTaskStatus() throws Exception {
        return createTaskStatus(testDtoForTaskStatus);
    }

    public ResultActions createDefaultTask() throws Exception {
        return createTask(buildDefaultTaskDto());
    }

    private TaskDto buildDefaultTaskDto() {
        final User user = userRepository.findByEmail(TEST_USERNAME).get();

        final TaskStatus taskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();

        final Label label = labelRepository.findByName(TEST_LABEL_NAME).get();

        final TaskDto taskDto = new TaskDto();
        taskDto.setName(TEST_TASK_NAME);
        taskDto.setDescription(TEST_TASK_DESCRIPTION);
        taskDto.setTaskStatusId(taskStatus.getId());
        taskDto.setAuthorId(user.getId());
        taskDto.setExecutorId(user.getId());
        taskDto.setLabelIds(Set.of(label.getId()));

        return taskDto;
    }

    public ResultActions createTaskStatus(final TaskStatusDto taskStatusDto) throws Exception {
        final MockHttpServletRequestBuilder request = post(TASK_STATUS_CONTROLLER_PATH)
            .content(asJson(taskStatusDto))
            .contentType(APPLICATION_JSON);

        return perform(request, TEST_USERNAME);
    }

    public ResultActions createTask(final TaskDto taskDto) throws Exception {
        final MockHttpServletRequestBuilder request = post(TASK_CONTROLLER_PATH)
            .content(MAPPER.writeValueAsString(taskDto))
            .contentType(APPLICATION_JSON);

        return perform(request, TEST_USERNAME);
    }

    public ResultActions createLabel(final LabelDto labelDto) throws Exception {
        final MockHttpServletRequestBuilder request = post(LABEL_CONTROLLER_PATH)
            .content(MAPPER.writeValueAsString(labelDto))
            .contentType(APPLICATION_JSON);

        return perform(request, TEST_USERNAME);
    }




    public static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}