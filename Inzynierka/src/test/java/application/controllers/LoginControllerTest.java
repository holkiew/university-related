package application.controllers;

import application.configuration.ControllersConstants;
import application.configuration.WebPropertiesLoader;
import application.controllers.models.responseModels.BasicResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DZONI on 20.12.2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginControllerTest {
    @Autowired
    WebPropertiesLoader webPropertiesLoader;
    /**
     * TEST NIE DZIALA BEZ PODLACZONEJ BAZY
     * w ogole nie dziala...
     */
    //:TODO zamockowac baze danych i zrobic test na tych mockach
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testLogin() {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("login", "username");
        arguments.put("password", "password");
        BasicResponse response1 = restTemplate.postForObject("http://localhost:8080/" + ControllersConstants.LOGIN_ENDPOINT, arguments, BasicResponse.class);
        System.out.println(response1);
//        arguments.put("token", response1.getResponseMessage());
//        BasicResponse response2 = restTemplate.postForObject("/" + ControllersConstants.LOGIN_ENDPOINT, arguments, BasicResponse.class);
//        assertTrue(response2.isSuccessful());
    }
}