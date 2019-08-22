package application.controllers.testControllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    public static void main(String[] args) {

    }

    @RequestMapping("/greeting")
    public Map<String, String> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        Map<String, String> temp = new HashMap<>();
        temp.put("K1", "V1");
        temp.put("K2", "V2");

        return temp;
    }
}