package hexlet.code;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.TestTaskStatusUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.instancio.Select;
import org.instancio.Instancio;
import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationTaskStatusTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    ObjectMapper om;

    @Autowired
    TaskStatusRepository taskStatusRepository;


    @BeforeEach
    public void setUp() throws Exception {
        TaskStatus testTaskStatus  = new TaskStatus("test-name-taskstatus");
        TestTaskStatusUtil.createTestTaskStatus(mockMvc, testTaskStatus);
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/api/statuses"))
            .andExpect(status().isOk());
    }

    @Test
    public void testShow() throws Exception {
        var taskStatus = Instancio.of(TaskStatus.class)
            .ignore(Select.field((TaskStatus::getId)))
            .supply(Select.field((TaskStatus::getName)), () -> faker.lorem().word())
            .create();

        taskStatusRepository.save(taskStatus);

        var request = get("/api/statuses/" + taskStatus.getId());
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var responseContent = result.getResponse().getContentAsString();
        var objectMapper = new ObjectMapper();
        var userFromResponse = objectMapper.readValue(responseContent, TaskStatus.class);
        assertThat(userFromResponse).isEqualTo(taskStatus);
    }

    @Test
    public void testCreate() throws Exception {
        var taskStatus = Instancio.of(TaskStatus.class)
            .ignore(Select.field((TaskStatus::getId)))
            .supply(Select.field((TaskStatus::getName)), () -> faker.lorem().word())
            .ignore(Select.field((TaskStatus::getCreatedAt)))
            .create();

        taskStatusRepository.save(taskStatus);

        var request = post("/api/statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(taskStatus));

        var result = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();
        var responseContent = result.getResponse().getContentAsString();
        var taskFromResponse = om.readValue(responseContent, TaskStatus.class);
        assertThat(taskFromResponse).isEqualTo(taskStatus);
    }

    @Test
    public void testUpdate() throws Exception {
        var taskStatus = Instancio.of(TaskStatus.class)
            .ignore(Select.field((TaskStatus::getId)))
            .supply(Select.field((TaskStatus::getName)), () -> faker.lorem().word())
            .ignore(Select.field((TaskStatus::getCreatedAt)))
            .create();

        taskStatusRepository.save(taskStatus);

        var taskStatusData = new HashMap<>();
        taskStatusData.put("name", "test-name-updated");

        var request = put("/api/statuses/" + taskStatus.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(taskStatusData));


        mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        var taskStatusUpdated = taskStatusRepository.findById(taskStatus.getId())
            .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id "
                + taskStatus.getId() + " not found"));
        assertThat(taskStatusUpdated.getName()).isEqualTo("test-name-updated");
    }

    @Test
    public void deleteTest() throws Exception {
        var taskStatus = Instancio.of(TaskStatus.class)
            .ignore(Select.field((TaskStatus::getId)))
            .supply(Select.field((TaskStatus::getName)), () -> faker.lorem().word())
            .ignore(Select.field((TaskStatus::getCreatedAt)))
            .create();
        taskStatusRepository.save(taskStatus);

        var request = delete("/api/statuses/" + taskStatus.getId());

        mockMvc.perform(request)
            .andExpect(status().isOk());

        var taskDeleted = taskStatusRepository.findById(taskStatus.getId());
        assertThat(taskDeleted.isPresent()).isEqualTo(false);
    }

}
