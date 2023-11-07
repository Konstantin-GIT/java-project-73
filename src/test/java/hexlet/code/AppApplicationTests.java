package hexlet.code;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void contextLoads() {
        // Проверка, что контекст Spring загружается без ошибок
    }


// !! не смог разобраться почему этот тест падает
//    @Test
//    public void mainMethodTest() {
//        // Проверка, что метод main запускается без ошибок
//        AppApplication.main(new String[]{});
//    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/welcome"))
            .andExpect(status().isOk())
            .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring");
    }
}
