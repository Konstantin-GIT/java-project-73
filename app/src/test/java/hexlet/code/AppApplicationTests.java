package hexlet.code;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.instancio.Select;
import org.mockito.Mock;
import org.instancio.Instancio;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    ObjectMapper om;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        User testUser = new User("test-firstName-user",
                                    "test-lastName-user",
                                    "test-email-user",
                                    "test-password-user");
        TestUtil.createTestPost(mockMvc, testUser);
    }

    @Test
    public void contextLoads() {
        // Проверка, что контекст Spring загружается без ошибок
    }

    @Test
    public void mainMethodTest() {
        // Проверка, что метод main запускается без ошибок
        AppApplication.main(new String[] {});
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());
    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/welcome"))
            .andExpect(status().isOk())
            .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring");
    }

    @Test
    public void testShow() throws Exception {
        var user = Instancio.of(User.class)
            .ignore(Select.field((User::getId)))
            .supply(Select.field((User::getFirstname)), () -> faker.lorem().word())
            .supply(Select.field((User::getLastname)), () -> faker.lorem().word())
            .supply(Select.field((User::getEmail)), () -> faker.internet().emailAddress())
            .create();

        userRepository.save(user);

        var request = get("/api/users/" + user.getId());
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var responseContent = result.getResponse().getContentAsString();
        var objectMapper = new ObjectMapper();
        var userFromResponse = objectMapper.readValue(responseContent, User.class);
        assertThat(userFromResponse).isEqualTo(user);
    }

    @Test
    public void testCreate() throws Exception {
        var user = Instancio.of(User.class)
            .ignore(Select.field((User::getId)))
            .supply(Select.field((User::getFirstname)), () -> faker.lorem().word())
            .supply(Select.field((User::getLastname)), () -> faker.lorem().word())
            .supply(Select.field((User::getEmail)), () -> faker.internet().emailAddress())
            .supply(Select.field((User::getPassword)), () -> faker.lorem().word())
            .create();

        userRepository.save(user);

        var request = post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(user));

        var result = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();
        var responseContent = result.getResponse().getContentAsString();
        var taskFromResponse = om.readValue(responseContent, User.class);
        assertThat(taskFromResponse).isEqualTo(user);
    }

    @Test
    public void testUpdate() throws Exception {
        var user = Instancio.of(User.class)
            .ignore(Select.field((User::getId)))
            .supply(Select.field((User::getFirstname)), () -> faker.lorem().word())
            .supply(Select.field((User::getLastname)), () -> faker.lorem().word())
            .supply(Select.field((User::getEmail)), () -> faker.internet().emailAddress())
            .supply(Select.field((User::getPassword)), () -> faker.lorem().word())
            .create();
        userRepository.save(user);

        var userData = new HashMap<>();
            userData.put("firstname","test-firstName-user-updated");
            userData.put("lastname", "test-lastName-user-updated");
            userData.put("email", "test-email-user-updated");
            userData.put("password", "test-password-user-updated");

        // не уверен что создавать экземпляр User для тестов через метод setUp() а потом доставать его - оптимально,
        // но хотелось как-то начать убирать дублирования кода в тестах.
        //В итоге метод findByFirstnameAndLastnameAndEmailAndPassword() не заработал(не находил User) и я закомментировал мето setUp()
       /* var user = userRepository
            .findByFirstnameAndLastnameAndEmailAndPassword("test-firstName-user", "test-lastName-user",
                "test-email-user", "test-password-user-updated")
            .orElseThrow(() -> new ResourceNotFoundException("User to perform testUdate() not found"));
*/
        var request = put("/api/users/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(userData));

        mockMvc.perform(request)
            .andExpect(status().isOk()).andReturn();

        var userUpdated = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + user.getId() + " not found"));
        assertThat(userUpdated.getFirstname()).isEqualTo(("test-firstName-user-updated"));
        assertThat(userUpdated.getLastname()).isEqualTo(("test-lastName-user-updated"));
        assertThat(userUpdated.getEmail()).isEqualTo(("test-email-user-updated"));
        assertThat(userUpdated.getPassword()).isEqualTo(("test-password-user-updated"));
    }

    @Test
    public void deleteTest() throws Exception {
        var user = Instancio.of(User.class)
            .ignore(Select.field((User::getId)))
            .supply(Select.field((User::getFirstname)), () -> faker.lorem().word())
            .supply(Select.field((User::getLastname)), () -> faker.lorem().word())
            .supply(Select.field((User::getEmail)), () -> faker.internet().emailAddress())
            .supply(Select.field((User::getPassword)), () -> faker.lorem().word())
            .create();
        userRepository.save(user);

        var request = delete("/api/users/" + user.getId());

        mockMvc.perform(request)
            .andExpect(status().isOk());

        var taskDeleted = userRepository.findById(user.getId());
        assertThat(taskDeleted.isPresent()).isEqualTo(false);
    }

}

