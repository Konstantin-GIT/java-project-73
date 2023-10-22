package hexlet.code;

import hexlet.code.DTO.UserDTO;
import hexlet.code.component.JWTHelper;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.TestUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.instancio.Select;
import org.instancio.Instancio;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationUserTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    ObjectMapper om;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JWTHelper jwtHelper;

    @Autowired
    private TestUserUtil utils;


    @BeforeEach
    public void setUp() throws Exception {
        User testUser = new User("test-firstName-user",
                                    "test-lastName-user",
                                    "test-email-user",
                                    "test-password-user");
        TestUserUtil.createTestPost(mockMvc, testUser);
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
            .supply(Select.field((User::getEmail)), () -> "test-email-user")
            .create();

        userRepository.save(user);

        var request = get("/api/users/" + user.getId());
        var result = utils.perform(request, "test-email-user").andExpect(status().isOk());

        var responseContent = result.andReturn().getResponse().getContentAsString();
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
            .supply(Select.field((User::getEmail)), () -> "test-email-user-updated")
            //.supply(Select.field((User::getPassword)), () -> faker.lorem().word())
            .create();
        userRepository.save(user);


        var userData = new HashMap<>();
        userData.put("firstname", "test-firstName-user-updated");
        userData.put("lastname", "test-lastName-user-updated");
        userData.put("email", "test-email-user-updated");
        //userData.put("password", "test-password-user-updated");


        var request = put("/api/users/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(userData));

        utils.perform(request, "test-email-user-updated").andExpect(status().isOk());


        var userUpdated = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + user.getId() + " not found"));

        //String passwordEncode = passwordEncoder.encode("test-password-user-updated");

        assertThat(userUpdated.getFirstname()).isEqualTo("test-firstName-user-updated");
        assertThat(userUpdated.getLastname()).isEqualTo("test-lastName-user-updated");
        assertThat(userUpdated.getEmail()).isEqualTo("test-email-user-updated");
        //assertThat(userUpdated.getPassword()).isEqualTo(passwordEncode);
    }

    @Test
    public void deleteTest() throws Exception {
        var user = Instancio.of(User.class)
            .ignore(Select.field((User::getId)))
            .supply(Select.field((User::getFirstname)), () -> faker.lorem().word())
            .supply(Select.field((User::getLastname)), () -> faker.lorem().word())
            .supply(Select.field((User::getEmail)), () -> "test-email-user-updated")
            .supply(Select.field((User::getPassword)), () -> faker.lorem().word())
            .create();
        userRepository.save(user);

        var request = delete("/api/users/" + user.getId());

        utils.perform(request, "test-email-user-updated").andExpect(status().isOk());

        var taskDeleted = userRepository.findById(user.getId());    
        assertThat(taskDeleted.isPresent()).isEqualTo(false);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

}

