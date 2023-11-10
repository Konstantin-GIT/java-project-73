package hexlet.code.controller;

import com.rollbar.notifier.Rollbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class WelcomeController {
    @Autowired
    Rollbar rollbar;
    @GetMapping(path = "/welcome")
    public String getGreeting() {
        rollbar.debug("Here is some debug message");
        return "Welcome to Spring";
    }
}
