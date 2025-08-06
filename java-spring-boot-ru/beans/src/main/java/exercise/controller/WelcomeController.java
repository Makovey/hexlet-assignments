package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// BEGIN
@RestController
public class WelcomeController {
    private final Daytime daytime;

    public WelcomeController(Daytime daytime) {
        this.daytime = daytime;
    }

    @GetMapping("/welcome")
    String welcome() {
        return "It is " + daytime.getName() + " now! Welcome to Spring!";
    }
}
// END
