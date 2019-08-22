package application.controllers;

import application.controllers.models.responseModels.BasicResponse;
import application.controllers.validators.CustomParseException;
import application.database.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import static application.configuration.ControllersConstants.CREATE_USER_ENDPOINT;

/**
 * Created by DZONI on 01.11.2016.
 */

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CreateUserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserService userEntityService;

    @RequestMapping(value = CREATE_USER_ENDPOINT, method = RequestMethod.POST)
    public BasicResponse createNewUser(
            @RequestParam(value = "accountType", required = false, defaultValue = "0") String accountType,
            @RequestParam(value = "company", required = false, defaultValue = "0") String company,
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "language", required = false, defaultValue = "0") String language,
            @RequestParam(value = "email", required = true) String email) {
        try {
            userEntityService.createNewUserAndSendActivationMail(accountType, company,
                    username, password, language, email);
        } catch (CustomParseException e) {
            logger.warn("Parsing error");
            return new BasicResponse(false, "Parsing error", 0);
        } catch (MailException e){
            logger.warn("Mail error");
            return new BasicResponse(false, "Couldn't send mail", 0);
        }
        return new BasicResponse(true, "Account created, mail sent", 0);
    }
}
