package hexlet.code.util;

import hexlet.code.model.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

public class TestTaskStatusUtil {
    private static ObjectMapper om = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    public static void createTestTaskStatus(MockMvc mockMvc, TaskStatus taskStatus) throws Exception {
        mockMvc.perform(
                post("/statuses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(taskStatus))
            )
            .andReturn()
            .getResponse();

    }
}

