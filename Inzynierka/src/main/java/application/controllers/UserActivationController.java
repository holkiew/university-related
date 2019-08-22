package application.controllers;

import application.database.entities.UserEntity;
import application.database.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static application.configuration.ControllersConstants.ACTIVATE_USER_USER_ENDPOINT;

/**
 * Created by DZONI on 08.11.2016.
 */
@RestController
public class UserActivationController {

    @Autowired
    UserService userEntityService;

    @RequestMapping(value = ACTIVATE_USER_USER_ENDPOINT + "/{user}/{activationCode}", method = RequestMethod.GET)
    public Boolean activateNewUser(@PathVariable(value = "user") String userId,
                                   @PathVariable(value = "activationCode") String activationCode) {
        try {
            UserEntity userEntity = userEntityService.getUserEntityById(userId);
            return userEntityService.activateUser(userEntity, activationCode);
        } catch (NumberFormatException ex) {
            return false;
        } catch (NullPointerException ex) {
            return false;
        }

    }
}
