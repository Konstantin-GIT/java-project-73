package hexlet.code.controller;

import com.rollbar.notifier.Rollbar;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "")
public class WelcomeController {

    private final Rollbar rollbar;
    @GetMapping(path = "/welcome")
    public String getGreeting() {
        rollbar.debug("Here is some debug message");
        return "Welcome to Spring";
    }
}
