package hexlet.code.util;

import hexlet.code.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

public class TestUtil {

    private static ObjectMapper om = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    public static void createTestPost(MockMvc mockMvc, User user) throws Exception {
        mockMvc.perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(user))
            )
            .andReturn()
            .getResponse();

    }
}
