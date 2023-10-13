package hexlet.code;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.fasterxml.jackson.databind.ObjectMapper;
//import net.datafaker.Faker;
//import org.instancio.Select;
//import org.mockito.Mock;
//import org.instancio.Instancio;
//import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationTests {

    @Autowired
    MockMvc mockMvc;


    @Autowired
    ObjectMapper om;

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/welcome"))
            .andExpect(status().isOk())
            .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring");
    }

/*
    @Test
    public void testShow() throws Exception {
        var task = Instancio.of(Task.class)
            .ignore(Select.field((Task::getId)))
            .supply(Select.field((Task::getDescription)), () -> faker.lorem().paragraph())
            .supply(Select.field((Task::getTitle)), () -> faker.lorem().word())
            .create();

        taskRepository.save(task);

        var request = get("/tasks/" + task.getId());

        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        var responseContent = result.getResponse().getContentAsString();

        var objectMapper = new ObjectMapper();
        var taskFromResponse = objectMapper.readValue(responseContent, Task.class);

        assertThat(taskFromResponse).isEqualTo(task);
    }

    @Test
    public void testCreate() throws Exception {
        var task = Instancio.of(Task.class)
            .ignore(Select.field((Task::getId)))
            .supply(Select.field((Task::getDescription)), () -> faker.lorem().paragraph())
            .supply(Select.field((Task::getTitle)), () -> faker.lorem().word())
            .create();


        var request = post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(task));

        var result = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();

        var responseContent = result.getResponse().getContentAsString();

        var objectMapper = new ObjectMapper();
        var taskFromResponse = objectMapper.readValue(responseContent, Task.class);

        assertThat(taskFromResponse).isEqualTo(task);
    }

    @Test
    public void testUdate() throws Exception {
        var task = Instancio.of(Task.class)
            .ignore(Select.field((Task::getId)))
            .supply(Select.field((Task::getDescription)), () -> faker.lorem().paragraph())
            .supply(Select.field((Task::getTitle)), () -> faker.lorem().word())
            .create();
        taskRepository.save(task);

        var taskData = new HashMap<>();
        taskData.put("title", "titleUpdate");
        taskData.put("description", "descriptionUpdate");


        var request = put("/tasks/" + task.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(taskData));

        mockMvc.perform(request)
            .andExpect(status().isOk());

        var taskUpdated = taskRepository.findById(task.getId()).get();
        assertThat(taskUpdated.getDescription()).isEqualTo(("descriptionUpdate"));
        assertThat(taskUpdated.getTitle()).isEqualTo(("titleUpdate"));
    }

    @Test
    public void deleteTest() throws Exception {
        var task = Instancio.of(Task.class)
            .ignore(Select.field((Task::getId)))
            .supply(Select.field((Task::getDescription)), () -> faker.lorem().paragraph())
            .supply(Select.field((Task::getTitle)), () -> faker.lorem().word())
            .create();
        taskRepository.save(task);

        var request = delete("/tasks/" + task.getId());

        mockMvc.perform(request)
            .andExpect(status().isOk());

        var taskDeleted = taskRepository.findById(task.getId());
        assertThat(taskDeleted.isPresent()).isEqualTo(false);
*/

}

