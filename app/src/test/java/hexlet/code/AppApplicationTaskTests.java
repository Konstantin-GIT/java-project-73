/*package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import hexlet.code.util.TestUserUtil;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationTaskTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    ObjectMapper om;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;

        @BeforeEach
        public void setUp() {
            //token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
            testTask = Instancio.of(modelGenerator.getTaskModel()).create();

            var testAuthor = Instancio.of(modelGenerator.getAuthorModel()).create();
            var testExecutor = Instancio.of(modelGenerator.getExecutorModel()).create();
            var testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();

            testTask.setAuthor(testAuthor);
            testTask.setExecutor(testExecutor);
            testTask.setTaskStatus(testTaskStatus);
           // testTask.setAuthor(userUtils.getTestUser());
        }



    //@Test
    @Test
    public void testIndex() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/posts"))//.with(token))
            .andExpect(status().isOk())
            .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        taskRepository.save(testTask);

        var request = get("/api/tasks/" + testTask.getId());
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var responseContent = result.getResponse().getContentAsString();
       // var objectMapper = new ObjectMapper();
        var taskFromResponse = om.readValue(responseContent, Task.class);
        assertThat(taskFromResponse).isEqualTo(testTask);
    }
}

*/